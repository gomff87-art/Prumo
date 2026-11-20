package com.zerovisual.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.LowFire;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ScreenEffectRenderer.class})
public class InGameOverlayRendererMixin {
   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("HEAD")}
   )
   private static void zerovisual$lowFirePush(PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
      matrices.pushPose();
      LowFire lowFire = (LowFire)ZeroVisualClient.get().modules().get(LowFire.class);
      if (lowFire.isEnabled()) {
         matrices.translate(0.0F, -lowFire.offset.getFloat(), 0.0F);
      }
   }

   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("RETURN")}
   )
   private static void zerovisual$lowFirePop(PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
      matrices.popPose();
   }
}
