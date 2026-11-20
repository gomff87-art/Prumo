package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.TotemPop;
import com.zerovisual.module.impl.visuals.ShieldColor;
import net.minecraft.class_1297;
import net.minecraft.class_2663;
import net.minecraft.class_634;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public class ClientPlayNetworkHandlerMixin {
   @Shadow
   private class_638 field_3699;

   @Inject(
      method = {"onEntityStatus"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V",
         shift = Shift.AFTER
      )}
   )
   private void zerovisual$totemPop(class_2663 packet, CallbackInfo ci) {
      if (this.field_3699 != null) {
         if (packet.method_11470() == 35) {
            class_1297 entity = packet.method_11469(this.field_3699);
            if (entity != null) {
               ((TotemPop)ZeroVisualClient.get().modules().get(TotemPop.class)).onPop(entity);
            }
         } else if (packet.method_11470() == 30) {
            class_1297 entity = packet.method_11469(this.field_3699);
            if (entity != null) {
               ((ShieldColor)ZeroVisualClient.get().modules().get(ShieldColor.class)).onShieldBreak(entity);
            }
         }
      }
   }
}
