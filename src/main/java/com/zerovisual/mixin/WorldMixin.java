package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.NoWeather;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1937.class})
public class WorldMixin {
   @Inject(
      method = {"getRainGradient", "getThunderGradient"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$noWeather(float delta, CallbackInfoReturnable<Float> cir) {
      if (((class_1937)this).method_8608()) {
         ZeroVisualClient client = ZeroVisualClient.get();
         if (client != null && ((NoWeather)client.modules().get(NoWeather.class)).isEnabled()) {
            cir.setReturnValue(0.0F);
         }
      }
   }
}
