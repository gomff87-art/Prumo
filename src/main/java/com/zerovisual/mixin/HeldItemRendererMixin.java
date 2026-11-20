package com.zerovisual.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Animations;
import com.zerovisual.module.impl.visuals.ViewModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemInHandRenderer.class})
public class HeldItemRendererMixin {
   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")}
   )
   private void zerovisual$viewModelPush(
      AbstractClientPlayer player,
      float tickDelta,
      float pitch,
      InteractionHand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      PoseStack matrices,
      MultiBufferSource vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      matrices.pushPose();
      ViewModel viewModel = (ViewModel)ZeroVisualClient.get().modules().get(ViewModel.class);
      if (viewModel.isEnabled()) {
         HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
         float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
         matrices.translate(viewModel.x.getFloat() * side, viewModel.y.getFloat(), viewModel.z.getFloat());
         float s = viewModel.scale.getFloat();
         matrices.scale(s, s, s);
      }
   }

   @Inject(
      method = {"swingArm"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$swingAnimation(float swingProgress, float equipProgress, PoseStack matrices, int armX, HumanoidArm arm, CallbackInfo ci) {
      Animations animations = (Animations)ZeroVisualClient.get().modules().get(Animations.class);
      if (animations.isEnabled() && !animations.mode.is("Vanilla")) {
         animations.applySwing(matrices, swingProgress, equipProgress, armX);
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("RETURN")}
   )
   private void zerovisual$viewModelPop(
      AbstractClientPlayer player,
      float tickDelta,
      float pitch,
      InteractionHand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      PoseStack matrices,
      MultiBufferSource vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      matrices.popPose();
   }
}
