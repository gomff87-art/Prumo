package com.zerovisual.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.performance.LiteDrops;
import com.zerovisual.module.impl.visuals.ItemPhysics;
import com.zerovisual.util.ItemPhysicsState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
public class ItemEntityRendererMixin {
   @Shadow
   @Final
   private RandomSource field_4725;

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$captureGround(ItemEntity entity, ItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
      ((ItemPhysicsState)state).zerovisual$setOnGround(entity.onGround());
      ((ItemPhysicsState)state).zerovisual$setVelocity((float)entity.getDeltaMovement().x, (float)entity.getDeltaMovement().z);
      if (((LiteDrops)ZeroVisualClient.get().modules().get(LiteDrops.class)).isEnabled()) {
         state.count = Math.min(state.count, 1);
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$itemPhysics(ItemEntityRenderState state, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null && ((ItemPhysics)client.modules().get(ItemPhysics.class)).isEnabled()) {
         if (!state.item.isEmpty()) {
            ItemPhysicsState physics = (ItemPhysicsState)state;
            if (!physics.zerovisual$isOnGround()) {
               this.renderFlying(state, physics, matrices, vertexConsumers, light);
               ci.cancel();
            } else {
               matrices.pushPose();
               matrices.method_22907(Axis.YP.rotation(state.bobOffset * 2.0F));
               float scaleY = state.item.method_65609().field_4285.y();
               if (state.item.method_65607()) {
                  matrices.translate(0.0F, 0.25F * scaleY, 0.0F);
               } else {
                  matrices.translate(0.0F, 0.02F, 0.0F);
                  matrices.method_22907(Axis.XP.rotationDegrees(-90.0F));
               }

               ItemEntityRenderer.method_56858(matrices, vertexConsumers, light, state, this.field_4725);
               matrices.popPose();
               ci.cancel();
            }
         }
      }
   }

   @Unique
   private void renderFlying(ItemEntityRenderState state, ItemPhysicsState physics, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
      float vx = physics.zerovisual$velocityX();
      float vz = physics.zerovisual$velocityZ();
      float speed = (float)Math.sqrt((double)(vx * vx + vz * vz));
      float yaw = speed > 0.01F ? (float)Math.atan2((double)vx, (double)vz) : state.bobOffset * 2.0F;
      float flip = state.ageInTicks * (12.0F + Math.min(speed, 1.0F) * 60.0F) + state.bobOffset * 57.0F;
      float scaleY = state.item.method_65609().field_4285.y();
      matrices.pushPose();
      matrices.translate(0.0F, 0.25F * scaleY, 0.0F);
      matrices.method_22907(Axis.YP.rotation(yaw));
      matrices.method_22907(Axis.XP.rotationDegrees(flip));
      if (!state.item.method_65607()) {
         matrices.method_22907(Axis.XP.rotationDegrees(-90.0F));
      }

      matrices.translate(0.0F, -0.25F * scaleY, 0.0F);
      ItemEntityRenderer.method_56858(matrices, vertexConsumers, light, state, this.field_4725);
      matrices.popPose();
   }
}
