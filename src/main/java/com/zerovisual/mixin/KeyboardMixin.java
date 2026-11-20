package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({KeyboardHandler.class})
public class KeyboardMixin {
   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")}
   )
   private void zerovisual$moduleBinds(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (action == 1 && window == mc.getWindow().handle() && mc.screen == null) {
         ZeroVisualClient.get().modules().onKeyPress(key);
      }
   }
}
