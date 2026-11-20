package com.zerovisual.mixin;

import com.zerovisual.gui.GlassTheme;
import com.zerovisual.render.ColorUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ColorUtil.class})
public abstract class GlassColorUtilMixin {
   @ModifyVariable(
      method = {"lerp"},
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 0
   )
   private static int zerovisual$glassLerpA(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"lerp"},
      at = @At("HEAD"),
      argsOnly = true,
      ordinal = 1
   )
   private static int zerovisual$glassLerpB(int var0) {
      return GlassTheme.map(var0);
   }

   @ModifyVariable(
      method = {"withAlpha"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private static int zerovisual$glassTint(int var0) {
      return GlassTheme.mapTint(var0);
   }
}
