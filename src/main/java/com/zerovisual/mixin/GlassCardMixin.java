package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.gui.GlassTheme;
import com.zerovisual.gui.Theme;
import com.zerovisual.module.impl.hud.HudStyle;
import com.zerovisual.render.ColorUtil;
import com.zerovisual.render.Render2D;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Theme.class})
public abstract class GlassCardMixin {
   @Inject(
      method = {"card(Lnet/minecraft/class_332;FFFFF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void zerovisual$glassCard(GuiGraphics var0, float var1, float var2, float var3, float var4, float var5, CallbackInfo var6) {
      GlassTheme.tick();
      HudStyle var7 = (HudStyle)ZeroVisualClient.get().modules().get(HudStyle.class);
      float var8 = Math.min(1.0F, var5 * var7.opacity.getFloat() / 0.88F);
      float var9 = var7.radius.getFloat();
      if (var8 > 0.01F) {
         if ((Boolean)var7.shadow.get()) {
            Render2D.shadow(var0, var1, var2 + 1.0F, var3, var4, var9, 6.0F, ColorUtil.fade(1879048192, var8));
         }

         Render2D.rect(var0, var1, var2, var3, var4, var9, ColorUtil.fade(GlassTheme.map(-1725273130), var8));
      }

      Render2D.outline(var0, var1, var2, var3, var4, var9, 1.0F, ColorUtil.fade(GlassTheme.map(1724901119), Math.max(var8, 0.0F)));
      var6.cancel();
   }
}
