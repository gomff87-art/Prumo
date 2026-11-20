package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zerovisual.gui.ButtonStyle;
import java.util.function.Function;
import net.minecraft.class_1921;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_357;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_357.class})
public abstract class SliderWidgetMixin extends class_339 {
   public SliderWidgetMixin(int x, int y, int width, int height, class_2561 message) {
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
      class_332 context, Function<class_2960, class_1921> layers, class_2960 sprite, int x, int y, int width, int height, int color, Operation<Void> original
   ) {
      if (!ButtonStyle.isEnabled()) {
         original.call(new Object[]{context, layers, sprite, x, y, width, height, color});
      } else {
         ButtonStyle.drawSliderTrack(context, (float)x, (float)y, (float)width, (float)height, this.field_22763, this.field_22765);
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
      class_332 context, Function<class_2960, class_1921> layers, class_2960 sprite, int x, int y, int width, int height, int color, Operation<Void> original
   ) {
      if (!ButtonStyle.isEnabled()) {
         original.call(new Object[]{context, layers, sprite, x, y, width, height, color});
      } else {
         ButtonStyle.drawSliderHandle(context, (float)x, (float)y, (float)width, (float)height, this.method_25367(), this.field_22765);
      }
   }
}
