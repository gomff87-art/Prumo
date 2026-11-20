package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.GlintColor;
import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4668.class})
public class RenderPhaseMixin {
   @Inject(
      method = {"setupGlintTexturing"},
      at = {@At("TAIL")}
   )
   private static void zerovisual$glintColor(float scale, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         GlintColor glint = (GlintColor)client.modules().get(GlintColor.class);
         if (glint.isEnabled()) {
            glint.apply();
         }
      }
   }
}
