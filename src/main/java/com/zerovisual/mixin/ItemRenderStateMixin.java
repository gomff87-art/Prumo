package com.zerovisual.mixin;

import com.zerovisual.util.ShieldOverlayHolder;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ItemStackRenderState.class})
public class ItemRenderStateMixin implements ShieldOverlayHolder {
   @Unique
   private int zerovisual$shieldOverlay = -1;

   public void zerovisual$setShieldOverlay(int overlay) {
      this.zerovisual$shieldOverlay = overlay;
   }

   @ModifyVariable(
      method = {"render"},
      at = {@At("HEAD")},
      argsOnly = true,
      ordinal = 1
   )
   private int zerovisual$shieldOverlay(int overlay) {
      return this.zerovisual$shieldOverlay != -1 ? this.zerovisual$shieldOverlay : overlay;
   }
}
