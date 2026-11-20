package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.CustomSky;
import net.minecraft.class_9975;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_9975.class})
public class SkyRenderingMixin {
   @Unique
   private static CustomSky zerovisual$sky() {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client == null) {
         return null;
      } else {
         CustomSky sky = (CustomSky)client.modules().get(CustomSky.class);
         return sky.isActive() ? sky : null;
      }
   }

   @Inject(
      method = {"renderSky"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$sky(float red, float green, float blue, CallbackInfo ci) {
      CustomSky sky = zerovisual$sky();
      if (sky != null) {
         sky.render();
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderGlowingSky", "renderCelestialBodies", "renderSkyDark"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$skipVanilla(CallbackInfo ci) {
      if (zerovisual$sky() != null) {
         ci.cancel();
      }
   }
}
