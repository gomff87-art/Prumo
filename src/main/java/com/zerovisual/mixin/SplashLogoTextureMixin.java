package com.zerovisual.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import com.zerovisual.ZeroVisualClient;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"net.minecraft.client.gui.screen.SplashOverlay$LogoTexture"}
)
public class SplashLogoTextureMixin {
   @Inject(
      method = {"loadContents"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$logo(ResourceManager resourceManager, CallbackInfoReturnable<TextureContents> cir) {
      try {
         try (InputStream stream = SplashLogoTextureMixin.class.getResourceAsStream("/assets/zerovisual/textures/gui/splash.png")) {
            if (stream != null) {
               cir.setReturnValue(new TextureContents(NativeImage.read(stream), new TextureMetadataSection(true, true)));
               return;
            }
         }
      } catch (Exception var8) {
         ZeroVisualClient.LOGGER.warn("Failed to load the ZeroVisual splash logo, using the vanilla one", var8);
      }
   }
}
