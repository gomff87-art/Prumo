package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.LowFire;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4603.class})
public class InGameOverlayRendererMixin {
   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("HEAD")}
   )
   private static void zerovisual$lowFirePush(class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      matrices.method_22903();
      LowFire lowFire = (LowFire)ZeroVisualClient.get().modules().get(LowFire.class);
      if (lowFire.isEnabled()) {
         matrices.method_46416(0.0F, -lowFire.offset.getFloat(), 0.0F);
      }
   }

   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("RETURN")}
   )
   private static void zerovisual$lowFirePop(class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      matrices.method_22909();
   }
}
