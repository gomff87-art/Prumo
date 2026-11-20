package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import java.io.InputStream;
import net.minecraft.class_1011;
import net.minecraft.class_10539;
import net.minecraft.class_1084;
import net.minecraft.class_3300;
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
   private void zerovisual$logo(class_3300 resourceManager, CallbackInfoReturnable<class_10539> cir) {
      try {
         try (InputStream stream = SplashLogoTextureMixin.class.getResourceAsStream("/assets/zerovisual/textures/gui/splash.png")) {
            if (stream != null) {
               cir.setReturnValue(new class_10539(class_1011.method_4309(stream), new class_1084(true, true)));
               return;
            }
         }
      } catch (Exception var8) {
         ZeroVisualClient.LOGGER.warn("Failed to load the ZeroVisual splash logo, using the vanilla one", var8);
      }
   }
}
