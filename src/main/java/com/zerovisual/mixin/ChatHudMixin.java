package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.hud.ChatHeads;
import com.zerovisual.module.impl.hud.ChatTweaks;
import java.util.List;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessage.Line;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatComponent.class})
public class ChatHudMixin {
   @Shadow
   @Final
   private List<Line> field_2064;
   @Unique
   private int zerovisual$lineCount;

   @Unique
   private static ChatTweaks zerovisual$tweaks() {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client == null) {
         return null;
      } else {
         ChatTweaks tweaks = (ChatTweaks)client.modules().get(ChatTweaks.class);
         return tweaks.isEnabled() ? tweaks : null;
      }
   }

   @ModifyVariable(
      method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"},
      at = {@At("HEAD")},
      argsOnly = true
   )
   private Component zerovisual$timestamp(Component message) {
      ChatTweaks tweaks = zerovisual$tweaks();
      return tweaks != null && tweaks.timestamps.get() ? tweaks.addTimestamp(message) : message;
   }

   @ModifyExpressionValue(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;",
         ordinal = 1
      )}
   )
   private Object zerovisual$background(Object value) {
      ChatTweaks tweaks = zerovisual$tweaks();
      return tweaks != null ? tweaks.background.get() : value;
   }

   @WrapOperation(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V",
         ordinal = 0
      ), @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V",
         ordinal = 1
      )}
   )
   private void zerovisual$slideFill(GuiGraphics context, int x1, int y1, int x2, int y2, int color, Operation<Void> original, @Local Line line) {
      float offset = zerovisual$offset(line);
      if (offset == 0.0F) {
         original.call(new Object[]{context, x1, y1, x2, y2, color});
      } else {
         context.method_51448().pushPose();
         context.method_51448().translate(offset, 0.0F, 0.0F);
         original.call(new Object[]{context, x1, y1, x2, y2, zerovisual$fade(color, offset)});
         context.method_51448().popPose();
      }
   }

   @WrapOperation(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
      )}
   )
   private int zerovisual$slideText(
      GuiGraphics context, Font renderer, FormattedCharSequence text, int x, int y, int color, Operation<Integer> original, @Local Line line
   ) {
      float offset = zerovisual$offset(line);
      ChatHeads heads = zerovisual$heads();
      if (offset == 0.0F && heads == null) {
         return (Integer)original.call(new Object[]{context, renderer, text, x, y, color});
      } else {
         int shown = offset == 0.0F ? color : zerovisual$fade(color, offset);
         context.method_51448().pushPose();
         context.method_51448().translate(offset, 0.0F, 0.0F);
         int shift = heads != null ? heads.drawHead(context, line, y, shown) : 0;
         int result = (Integer)original.call(new Object[]{context, renderer, text, x + shift, y, shown});
         context.method_51448().popPose();
         return result;
      }
   }

   @Inject(
      method = {"addVisibleMessage"},
      at = {@At("HEAD")}
   )
   private void zerovisual$headsBegin(GuiMessage message, CallbackInfo ci) {
      ChatHeads heads = zerovisual$heads();
      if (heads != null) {
         heads.beginMessage(message.content());
      }
   }

   @ModifyArg(
      method = {"addVisibleMessage"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;"
      )},
      index = 1
   )
   private int zerovisual$headsWidth(int width) {
      ChatHeads heads = zerovisual$heads();
      return heads != null ? heads.wrapWidth(width) : width;
   }

   @ModifyExpressionValue(
      method = {"addVisibleMessage"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;"
      )}
   )
   private List<FormattedCharSequence> zerovisual$headsLines(List<FormattedCharSequence> lines) {
      this.zerovisual$lineCount = lines.size();
      return lines;
   }

   @Inject(
      method = {"addVisibleMessage"},
      at = {@At("RETURN")}
   )
   private void zerovisual$headsEnd(GuiMessage message, CallbackInfo ci) {
      ChatHeads heads = zerovisual$heads();
      if (heads != null) {
         heads.endMessage(this.field_2064, this.zerovisual$lineCount);
      }
   }

   @Unique
   private static ChatHeads zerovisual$heads() {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client == null) {
         return null;
      } else {
         ChatHeads heads = (ChatHeads)client.modules().get(ChatHeads.class);
         return heads.isEnabled() ? heads : null;
      }
   }

   @Unique
   private static float zerovisual$offset(Line line) {
      ChatTweaks tweaks = zerovisual$tweaks();
      return tweaks != null && line != null ? tweaks.lineOffset(line) : 0.0F;
   }

   @Unique
   private static int zerovisual$fade(int color, float offset) {
      float t = 1.0F + offset / 40.0F;
      int alpha = Math.max(4, Math.round((float)(color >>> 24 & 0xFF) * t));
      return alpha << 24 | color & 16777215;
   }
}
