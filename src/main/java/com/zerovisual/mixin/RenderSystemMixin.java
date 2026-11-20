package com.zerovisual.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.GlintColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {RenderSystem.class},
   remap = false
)
public class RenderSystemMixin {
   @Inject(
      method = {"resetTextureMatrix"},
      at = {@At("TAIL")}
   )
   private static void zerovisual$glintReset(CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         ((GlintColor)client.modules().get(GlintColor.class)).reset();
      }
   }
}
