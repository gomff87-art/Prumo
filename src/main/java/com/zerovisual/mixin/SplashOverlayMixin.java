package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_425;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_425.class})
public class SplashOverlayMixin {
   @ModifyExpressionValue(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/function/IntSupplier;getAsInt()I"
      )}
   )
   private int zerovisual$darkBackground(int brandColor) {
      return -16250868;
   }
}
