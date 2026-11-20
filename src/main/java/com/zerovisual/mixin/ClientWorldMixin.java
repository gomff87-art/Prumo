package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Ambience;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ClientLevel.class})
public class ClientWorldMixin {
   @ModifyReturnValue(
      method = {"getSkyColor"},
      at = {@At("RETURN")}
   )
   private int zerovisual$skyColor(int color) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client == null) {
         return color;
      } else {
         Ambience ambience = (Ambience)client.modules().get(Ambience.class);
         return ambience.isEnabled() ? ambience.tintSky(color) : color;
      }
   }
}
