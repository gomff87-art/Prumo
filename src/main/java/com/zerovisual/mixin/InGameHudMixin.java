package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.hud.Potions;
import com.zerovisual.module.impl.hud.Saturation;
import com.zerovisual.module.impl.visuals.Crosshair;
import net.minecraft.class_1657;
import net.minecraft.class_1934;
import net.minecraft.class_239;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_340;
import net.minecraft.class_9779;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public abstract class InGameHudMixin {
   @Shadow
   @Final
   private class_310 field_2035;
   @Shadow
   @Final
   private class_340 field_2026;

   @Shadow
   protected abstract boolean method_17534(@Nullable class_239 var1);

   @Inject(
      method = {"renderStatusEffectOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$hideVanillaEffects(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Potions potions = (Potions)ZeroVisualClient.get().modules().get(Potions.class);
      if (potions.isEnabled() && (Boolean)potions.hideVanilla.get()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderFood"},
      at = {@At("TAIL")}
   )
   private void zerovisual$saturation(class_332 context, class_1657 player, int top, int right, CallbackInfo ci) {
      Saturation saturation = (Saturation)ZeroVisualClient.get().modules().get(Saturation.class);
      if (saturation.isEnabled()) {
         saturation.draw(context, player, top, right);
      }
   }

   @Inject(
      method = {"renderCrosshair"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$customCrosshair(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Crosshair crosshair = (Crosshair)ZeroVisualClient.get().modules().get(Crosshair.class);
      if (crosshair.isEnabled() && !this.field_2026.method_53536()) {
         ci.cancel();
         if (this.field_2035.field_1690.method_31044().method_31034()) {
            if (this.field_2035.field_1761.method_2920() != class_1934.field_9219 || this.method_17534(this.field_2035.field_1765)) {
               crosshair.draw(context);
            }
         }
      }
   }
}
