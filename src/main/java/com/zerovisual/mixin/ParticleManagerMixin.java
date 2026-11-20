package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.performance.CrystalOptimizer;
import com.zerovisual.module.impl.performance.ParticleLimit;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ParticleEngine.class})
public class ParticleManagerMixin {
   @Inject(
      method = {"addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$noExplosions(
      ParticleOptions effect, double x, double y, double z, double vx, double vy, double vz, CallbackInfoReturnable<Particle> cir
   ) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         CrystalOptimizer crystals = (CrystalOptimizer)client.modules().get(CrystalOptimizer.class);
         if (crystals.isEnabled()
            && (Boolean)crystals.noExplosions.get()
            && (effect.getType() == ParticleTypes.EXPLOSION || effect.getType() == ParticleTypes.EXPLOSION_EMITTER)) {
            cir.setReturnValue(null);
         }
      }
   }

   @Inject(
      method = {"addParticle(Lnet/minecraft/client/particle/Particle;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$limit(Particle particle, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         ParticleLimit limit = (ParticleLimit)client.modules().get(ParticleLimit.class);
         if (limit.isEnabled() && limit.skip()) {
            ci.cancel();
         }
      }
   }
}
