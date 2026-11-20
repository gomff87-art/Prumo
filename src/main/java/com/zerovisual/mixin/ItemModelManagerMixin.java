package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.ShieldColor;
import com.zerovisual.util.ShieldOverlayHolder;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemModelResolver.class})
public class ItemModelManagerMixin {
   @Inject(
      method = {"update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$shieldColor(
      ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext mode, boolean leftHand, Level world, LivingEntity entity, int seed, CallbackInfo ci
   ) {
      ZeroVisualClient client = ZeroVisualClient.get();
      int overlay = client == null ? -1 : ((ShieldColor)client.modules().get(ShieldColor.class)).overlayFor(stack, entity);
      ((ShieldOverlayHolder)renderState).zerovisual$setShieldOverlay(overlay);
   }
}
