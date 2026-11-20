package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.hud.ChatHeads;
import com.zerovisual.module.impl.hud.ChatTweaks;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_303;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_338;
import net.minecraft.class_5481;
import net.minecraft.class_303.class_7590;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_338.class})
public class ChatHudMixin {
   @Shadow
   @Final
   private List<class_7590> field_2064;
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
   private class_2561 zerovisual$timestamp(class_2561 message) {
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
   private void zerovisual$slideFill(class_332 context, int x1, int y1, int x2, int y2, int color, Operation<Void> original, @Local class_7590 line) {
      float offset = zerovisual$offset(line);
      if (offset == 0.0F) {
         original.call(new Object[]{context, x1, y1, x2, y2, color});
      } else {
         context.method_51448().method_22903();
         context.method_51448().method_46416(offset, 0.0F, 0.0F);
         original.call(new Object[]{context, x1, y1, x2, y2, zerovisual$fade(color, offset)});
         context.method_51448().method_22909();
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
      class_332 context, class_327 renderer, class_5481 text, int x, int y, int color, Operation<Integer> original, @Local class_7590 line
   ) {
      float offset = zerovisual$offset(line);
      ChatHeads heads = zerovisual$heads();
      if (offset == 0.0F && heads == null) {
         return (Integer)original.call(new Object[]{context, renderer, text, x, y, color});
      } else {
         int shown = offset == 0.0F ? color : zerovisual$fade(color, offset);
         context.method_51448().method_22903();
         context.method_51448().method_46416(offset, 0.0F, 0.0F);
         int shift = heads != null ? heads.drawHead(context, line, y, shown) : 0;
         int result = (Integer)original.call(new Object[]{context, renderer, text, x + shift, y, shown});
         context.method_51448().method_22909();
         return result;
      }
   }

   @Inject(
      method = {"addVisibleMessage"},
      at = {@At("HEAD")}
   )
   private void zerovisual$headsBegin(class_303 message, CallbackInfo ci) {
      ChatHeads heads = zerovisual$heads();
      if (heads != null) {
         heads.beginMessage(message.comp_893());
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
   private List<class_5481> zerovisual$headsLines(List<class_5481> lines) {
      this.zerovisual$lineCount = lines.size();
      return lines;
   }

   @Inject(
      method = {"addVisibleMessage"},
      at = {@At("RETURN")}
   )
   private void zerovisual$headsEnd(class_303 message, CallbackInfo ci) {
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
   private static float zerovisual$offset(class_7590 line) {
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
