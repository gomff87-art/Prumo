package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.KillEffect;
import com.zerovisual.module.impl.performance.EntityCulling;
import com.zerovisual.module.impl.visuals.HitboxColor;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({EntityRenderDispatcher.class})
public class EntityRenderDispatcherMixin {
   @Inject(
      method = {"shouldRender"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private <E extends Entity> void zerovisual$hideDying(E entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         if (((KillEffect)client.modules().get(KillEffect.class)).isHidden(entity)) {
            cir.setReturnValue(false);
         } else {
            EntityCulling culling = (EntityCulling)client.modules().get(EntityCulling.class);
            if (culling.isEnabled() && culling.hides(entity, x, y, z)) {
               cir.setReturnValue(false);
            }
         }
      }
   }

   @ModifyArgs(
      method = {"render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderHitbox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;FFFF)V",
         ordinal = 0
      )}
   )
   private void zerovisual$hitboxColor(Args args) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         HitboxColor hitbox = (HitboxColor)client.modules().get(HitboxColor.class);
         if (hitbox.recolors((Entity)args.get(2))) {
            int c = hitbox.boxColor();
            args.set(4, (float)(c >> 16 & 0xFF) / 255.0F);
            args.set(5, (float)(c >> 8 & 0xFF) / 255.0F);
            args.set(6, (float)(c & 0xFF) / 255.0F);
         }
      }
   }
}
