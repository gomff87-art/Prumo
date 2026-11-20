package com.zerovisual.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.emote.EmoteManager;
import com.zerovisual.module.impl.visuals.Nametags;
import com.zerovisual.util.EmoteState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AvatarRenderer.class})
public class PlayerEntityRendererMixin {
   @Inject(
      method = {"renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$nametags(AvatarRenderState state, Component text, PoseStack matrices, MultiBufferSource consumers, int light, CallbackInfo ci) {
      Nametags nametags = (Nametags)ZeroVisualClient.get().modules().get(Nametags.class);
      if (nametags.isEnabled() && nametags.capture(state, text)) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$emotePose(AbstractClientPlayer player, AvatarRenderState state, float tickDelta, CallbackInfo ci) {
      EmoteState emote = (EmoteState)state;
      emote.zerovisual$setWeight(EmoteManager.sample(player.getUUID(), emote.zerovisual$pose()));
      if (emote.zerovisual$weight() > 0.0F) {
         state.rightHandItemState.clear();
         state.leftHandItemState.clear();
      }
   }
}
