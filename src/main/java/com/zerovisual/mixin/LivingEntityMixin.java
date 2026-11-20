package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Animations;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_1309.class})
public class LivingEntityMixin {
   @ModifyReturnValue(
      method = {"getHandSwingDuration"},
      at = {@At("RETURN")}
   )
   private int zerovisual$swingSpeed(int duration) {
      if (this != class_310.method_1551().field_1724) {
         return duration;
      } else {
         ZeroVisualClient client = ZeroVisualClient.get();
         if (client == null) {
            return duration;
         } else {
            Animations animations = (Animations)client.modules().get(Animations.class);
            return animations.isEnabled() ? animations.adjustSwingDuration(duration) : duration;
         }
      }
   }
}
