package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.utilities.ScreenshotPlus;
import java.io.File;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_276;
import net.minecraft.class_318;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_318.class})
public class ScreenshotRecorderMixin {
   @Inject(
      method = {"saveScreenshot(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V"},
      at = {@At("HEAD")}
   )
   private static void zerovisual$screenshotPlus(
      File gameDirectory, @Nullable String fileName, class_276 framebuffer, Consumer<class_2561> messageReceiver, CallbackInfo ci
   ) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null && fileName == null) {
         ScreenshotPlus screenshotPlus = (ScreenshotPlus)client.modules().get(ScreenshotPlus.class);
         if (screenshotPlus.isEnabled()) {
            screenshotPlus.onScreenshot(gameDirectory);
         }
      }
   }
}
