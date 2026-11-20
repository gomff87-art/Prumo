package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Freelook;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1297.class})
public class EntityMixin {
   @Inject(
      method = {"changeLookDirection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$freelook(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
      if (this == class_310.method_1551().field_1724) {
         ZeroVisualClient client = ZeroVisualClient.get();
         if (client != null) {
            Freelook freelook = (Freelook)client.modules().get(Freelook.class);
            if (freelook.isActive()) {
               freelook.rotate(cursorDeltaX, cursorDeltaY);
               ci.cancel();
            }
         }
      }
   }
}
