package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zerovisual.gui.ButtonStyle;
import java.util.function.Function;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({AbstractButton.class})
public abstract class PressableWidgetMixin extends AbstractWidget {
   public PressableWidgetMixin(int x, int y, int width, int height, Component message) {
      super(x, y, width, height, message);
   }

   @WrapOperation(
      method = {"renderWidget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIIII)V"
      )}
   )
   private void zerovisual$background(
      GuiGraphics context, Function<Identifier, RenderType> layers, Identifier sprite, int x, int y, int width, int height, int color, Operation<Void> original
   ) {
      if (!ButtonStyle.isEnabled()) {
         original.call(new Object[]{context, layers, sprite, x, y, width, height, color});
      } else {
         ButtonStyle.drawButton(context, this, (float)x, (float)y, (float)width, (float)height, this.active, this.isHoveredOrFocused(), this.alpha);
      }
   }

   @WrapOperation(
      method = {"renderWidget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/widget/PressableWidget;drawMessage(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;I)V"
      )}
   )
   private void zerovisual$label(AbstractButton widget, GuiGraphics context, Font textRenderer, int color, Operation<Void> original) {
      if (ButtonStyle.isEnabled() && !(widget instanceof SpriteIconButton)) {
         ButtonStyle.drawLabel(
            context,
            this,
            this.getMessage(),
            (float)this.getX(),
            (float)this.getY(),
            (float)this.getWidth(),
            (float)this.getHeight(),
            this.active,
            this.isHoveredOrFocused(),
            this.alpha
         );
      } else {
         original.call(new Object[]{widget, context, textRenderer, color});
      }
   }
}
