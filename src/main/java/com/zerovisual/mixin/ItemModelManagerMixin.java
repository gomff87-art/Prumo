package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.ShieldColor;
import com.zerovisual.util.ShieldOverlayHolder;
import net.minecraft.class_10442;
import net.minecraft.class_10444;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_811;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_10442.class})
public class ItemModelManagerMixin {
   @Inject(
      method = {"update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$shieldColor(
      class_10444 renderState, class_1799 stack, class_811 mode, boolean leftHand, class_1937 world, class_1309 entity, int seed, CallbackInfo ci
   ) {
      ZeroVisualClient client = ZeroVisualClient.get();
      int overlay = client == null ? -1 : ((ShieldColor)client.modules().get(ShieldColor.class)).overlayFor(stack, entity);
      ((ShieldOverlayHolder)renderState).zerovisual$setShieldOverlay(overlay);
   }
}
