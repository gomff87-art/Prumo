package com.zerovisual.mixin;

import com.zerovisual.gui.GlassTheme;
import com.zerovisual.render.Render2D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({Render2D.class})
public abstract class GlassRender2DMixin {
   @ModifyVariable(
      method = {"rect"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private static int zerovisual$glassRect(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"outline"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private static int zerovisual$glassOutline(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"gradientH"},
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private static int zerovisual$glassGradHL(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"gradientH"},
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 1
   )
   private static int zerovisual$glassGradHR(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"gradientV"},
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private static int zerovisual$glassGradVT(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"gradientV"},
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 1
   )
   private static int zerovisual$glassGradVB(int var0) {
      return GlassTheme.map(var0);
   }
}
