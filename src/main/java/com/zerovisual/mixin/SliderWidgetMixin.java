package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zerovisual.gui.ButtonStyle;
import java.util.function.Function;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({AbstractSliderButton.class})
public abstract class SliderWidgetMixin extends AbstractWidget {
   public SliderWidgetMixin(int x, int y, int width, int height, Component message) {
      super(x, y, width, height, message);
   }

   @WrapOperation(
      method = {"renderWidget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIIII)V",
         ordinal = 0
      )}
   )
   private void zerovisual$track(
      GuiGraphics context, Function<Identifier, RenderType> layers, Identifier sprite, int x, int y, int width, int height, int color, Operation<Void> original
   ) {
      if (!ButtonStyle.isEnabled()) {
         original.call(new Object[]{context, layers, sprite, x, y, width, height, color});
      } else {
         ButtonStyle.drawSliderTrack(context, (float)x, (float)y, (float)width, (float)height, this.active, this.alpha);
      }
   }

   @WrapOperation(
      method = {"renderWidget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIIII)V",
         ordinal = 1
      )}
   )
   private void zerovisual$handle(
      GuiGraphics context, Function<Identifier, RenderType> layers, Identifier sprite, int x, int y, int width, int height, int color, Operation<Void> original
   ) {
      if (!ButtonStyle.isEnabled()) {
         original.call(new Object[]{context, layers, sprite, x, y, width, height, color});
      } else {
         ButtonStyle.drawSliderHandle(context, (float)x, (float)y, (float)width, (float)height, this.isHoveredOrFocused(), this.alpha);
      }
   }
}
