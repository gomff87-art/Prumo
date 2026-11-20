package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import net.minecraft.class_309;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_309.class})
public class KeyboardMixin {
   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")}
   )
   private void zerovisual$moduleBinds(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      if (action == 1 && window == mc.method_22683().method_4490() && mc.field_1755 == null) {
         ZeroVisualClient.get().modules().onKeyPress(key);
      }
   }
}
