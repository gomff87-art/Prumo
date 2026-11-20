package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.Cape;
import net.minecraft.class_310;
import net.minecraft.class_742;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_742.class})
public class AbstractClientPlayerEntityMixin {
   @ModifyReturnValue(
      method = {"getSkinTextures"},
      at = {@At("RETURN")}
   )
   private class_8685 zerovisual$cape(class_8685 textures) {
      if (this != class_310.method_1551().field_1724) {
         return textures;
      } else {
         ZeroVisualClient client = ZeroVisualClient.get();
         if (client == null) {
            return textures;
         } else {
            Cape cape = (Cape)client.modules().get(Cape.class);
            return cape.isEnabled() ? cape.apply(textures) : textures;
         }
      }
   }
}
