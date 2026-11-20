package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.performance.CrystalOptimizer;
import net.minecraft.class_10014;
import net.minecraft.class_1511;
import net.minecraft.class_892;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_892.class})
public class EndCrystalRendererMixin {
   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/decoration/EndCrystalEntity;Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$crystal(class_1511 entity, class_10014 state, float tickDelta, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         CrystalOptimizer crystals = (CrystalOptimizer)client.modules().get(CrystalOptimizer.class);
         if (crystals.isEnabled()) {
            if ((Boolean)crystals.still.get()) {
               state.field_53328 = 0.0F;
            }

            if ((Boolean)crystals.noBeam.get()) {
               state.field_53312 = null;
            }
         }
      }
   }
}
