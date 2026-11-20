package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.performance.CrystalOptimizer;
import com.zerovisual.module.impl.performance.ParticleLimit;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_702.class})
public class ParticleManagerMixin {
   @Inject(
      method = {"addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$noExplosions(class_2394 effect, double x, double y, double z, double vx, double vy, double vz, CallbackInfoReturnable<class_703> cir) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         CrystalOptimizer crystals = (CrystalOptimizer)client.modules().get(CrystalOptimizer.class);
         if (crystals.isEnabled()
            && (Boolean)crystals.noExplosions.get()
            && (effect.method_10295() == class_2398.field_11236 || effect.method_10295() == class_2398.field_11221)) {
            cir.setReturnValue(null);
         }
      }
   }

   @Inject(
      method = {"addParticle(Lnet/minecraft/client/particle/Particle;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$limit(class_703 particle, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         ParticleLimit limit = (ParticleLimit)client.modules().get(ParticleLimit.class);
         if (limit.isEnabled() && limit.skip()) {
            ci.cancel();
         }
      }
   }
}
