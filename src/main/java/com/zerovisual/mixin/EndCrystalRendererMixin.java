package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.performance.CrystalOptimizer;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EndCrystalRenderer.class})
public class EndCrystalRendererMixin {
   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/decoration/EndCrystalEntity;Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$crystal(EndCrystal entity, EndCrystalRenderState state, float tickDelta, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         CrystalOptimizer crystals = (CrystalOptimizer)client.modules().get(CrystalOptimizer.class);
         if (crystals.isEnabled()) {
            if ((Boolean)crystals.still.get()) {
               state.ageInTicks = 0.0F;
            }

            if ((Boolean)crystals.noBeam.get()) {
               state.beamOffset = null;
            }
         }
      }
   }
}
