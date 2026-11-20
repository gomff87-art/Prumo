package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.Cape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({AbstractClientPlayer.class})
public class AbstractClientPlayerEntityMixin {
   @ModifyReturnValue(
      method = {"getSkinTextures"},
      at = {@At("RETURN")}
   )
   private PlayerSkin zerovisual$cape(PlayerSkin textures) {
      if (this != Minecraft.getInstance().player) {
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
