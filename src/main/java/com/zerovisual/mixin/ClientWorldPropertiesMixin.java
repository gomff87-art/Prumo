package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.TimeChanger;
import net.minecraft.class_638.class_5271;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_5271.class})
public class ClientWorldPropertiesMixin {
   @Inject(
      method = {"getTimeOfDay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$timeChanger(CallbackInfoReturnable<Long> cir) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         TimeChanger timeChanger = (TimeChanger)client.modules().get(TimeChanger.class);
         if (timeChanger.isEnabled()) {
            cir.setReturnValue((long)timeChanger.time.getInt());
         }
      }
   }
}
