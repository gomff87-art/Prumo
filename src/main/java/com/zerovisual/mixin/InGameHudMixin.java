package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.hud.Potions;
import com.zerovisual.module.impl.hud.Saturation;
import com.zerovisual.module.impl.visuals.Crosshair;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Gui.class})
public abstract class InGameHudMixin {
   @Shadow
   @Final
   private Minecraft field_2035;
   @Shadow
   @Final
   private DebugScreenOverlay field_2026;

   @Shadow
   protected abstract boolean method_17534(@Nullable HitResult var1);

   @Inject(
      method = {"renderStatusEffectOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$hideVanillaEffects(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
      Potions potions = (Potions)ZeroVisualClient.get().modules().get(Potions.class);
      if (potions.isEnabled() && (Boolean)potions.hideVanilla.get()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderFood"},
      at = {@At("TAIL")}
   )
   private void zerovisual$saturation(GuiGraphics context, Player player, int top, int right, CallbackInfo ci) {
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
   private void zerovisual$customCrosshair(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
      Crosshair crosshair = (Crosshair)ZeroVisualClient.get().modules().get(Crosshair.class);
      if (crosshair.isEnabled() && !this.field_2026.showDebugScreen()) {
         ci.cancel();
         if (this.field_2035.options.getCameraType().isFirstPerson()) {
            if (this.field_2035.gameMode.getPlayerMode() != GameType.SPECTATOR || this.method_17534(this.field_2035.hitResult)) {
               crosshair.draw(context);
            }
         }
      }
   }
}
