package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Freelook;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public class EntityMixin {
   @Inject(
      method = {"changeLookDirection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$freelook(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
      if (this == Minecraft.getInstance().player) {
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
