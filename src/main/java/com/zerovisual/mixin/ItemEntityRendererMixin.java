package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.performance.LiteDrops;
import com.zerovisual.module.impl.visuals.ItemPhysics;
import com.zerovisual.util.ItemPhysicsState;
import net.minecraft.class_10039;
import net.minecraft.class_1542;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5819;
import net.minecraft.class_7833;
import net.minecraft.class_916;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_916.class})
public class ItemEntityRendererMixin {
   @Shadow
   @Final
   private class_5819 field_4725;

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$captureGround(class_1542 entity, class_10039 state, float tickDelta, CallbackInfo ci) {
      ((ItemPhysicsState)state).zerovisual$setOnGround(entity.method_24828());
      ((ItemPhysicsState)state).zerovisual$setVelocity((float)entity.method_18798().field_1352, (float)entity.method_18798().field_1350);
      if (((LiteDrops)ZeroVisualClient.get().modules().get(LiteDrops.class)).isEnabled()) {
         state.field_55311 = Math.min(state.field_55311, 1);
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$itemPhysics(class_10039 state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null && ((ItemPhysics)client.modules().get(ItemPhysics.class)).isEnabled()) {
         if (!state.field_55310.method_65606()) {
            ItemPhysicsState physics = (ItemPhysicsState)state;
            if (!physics.zerovisual$isOnGround()) {
               this.renderFlying(state, physics, matrices, vertexConsumers, light);
               ci.cancel();
            } else {
               matrices.method_22903();
               matrices.method_22907(class_7833.field_40716.rotation(state.field_53435 * 2.0F));
               float scaleY = state.field_55310.method_65609().field_4285.y();
               if (state.field_55310.method_65607()) {
                  matrices.method_46416(0.0F, 0.25F * scaleY, 0.0F);
               } else {
                  matrices.method_46416(0.0F, 0.02F, 0.0F);
                  matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0F));
               }

               class_916.method_56858(matrices, vertexConsumers, light, state, this.field_4725);
               matrices.method_22909();
               ci.cancel();
            }
         }
      }
   }

   @Unique
   private void renderFlying(class_10039 state, ItemPhysicsState physics, class_4587 matrices, class_4597 vertexConsumers, int light) {
      float vx = physics.zerovisual$velocityX();
      float vz = physics.zerovisual$velocityZ();
      float speed = (float)Math.sqrt((double)(vx * vx + vz * vz));
      float yaw = speed > 0.01F ? (float)Math.atan2((double)vx, (double)vz) : state.field_53435 * 2.0F;
      float flip = state.field_53328 * (12.0F + Math.min(speed, 1.0F) * 60.0F) + state.field_53435 * 57.0F;
      float scaleY = state.field_55310.method_65609().field_4285.y();
      matrices.method_22903();
      matrices.method_46416(0.0F, 0.25F * scaleY, 0.0F);
      matrices.method_22907(class_7833.field_40716.rotation(yaw));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(flip));
      if (!state.field_55310.method_65607()) {
         matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0F));
      }

      matrices.method_46416(0.0F, -0.25F * scaleY, 0.0F);
      class_916.method_56858(matrices, vertexConsumers, light, state, this.field_4725);
      matrices.method_22909();
   }
}
