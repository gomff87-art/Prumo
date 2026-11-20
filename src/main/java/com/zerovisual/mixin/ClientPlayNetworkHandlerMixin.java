package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.TotemPop;
import com.zerovisual.module.impl.visuals.ShieldColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPacketListener.class})
public class ClientPlayNetworkHandlerMixin {
   @Shadow
   private ClientLevel field_3699;

   @Inject(
      method = {"onEntityStatus"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V",
         shift = Shift.AFTER
      )}
   )
   private void zerovisual$totemPop(ClientboundEntityEventPacket packet, CallbackInfo ci) {
      if (this.field_3699 != null) {
         if (packet.getEventId() == 35) {
            Entity entity = packet.getEntity(this.field_3699);
            if (entity != null) {
               ((TotemPop)ZeroVisualClient.get().modules().get(TotemPop.class)).onPop(entity);
            }
         } else if (packet.getEventId() == 30) {
            Entity entity = packet.getEntity(this.field_3699);
            if (entity != null) {
               ((ShieldColor)ZeroVisualClient.get().modules().get(ShieldColor.class)).onShieldBreak(entity);
            }
         }
      }
   }
}
