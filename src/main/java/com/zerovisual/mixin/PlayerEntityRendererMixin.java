package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.emote.EmoteManager;
import com.zerovisual.module.impl.visuals.Nametags;
import com.zerovisual.util.EmoteState;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1007.class})
public class PlayerEntityRendererMixin {
   @Inject(
      method = {"renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$nametags(class_10055 state, class_2561 text, class_4587 matrices, class_4597 consumers, int light, CallbackInfo ci) {
      Nametags nametags = (Nametags)ZeroVisualClient.get().modules().get(Nametags.class);
      if (nametags.isEnabled() && nametags.capture(state, text)) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$emotePose(class_742 player, class_10055 state, float tickDelta, CallbackInfo ci) {
      EmoteState emote = (EmoteState)state;
      emote.zerovisual$setWeight(EmoteManager.sample(player.method_5667(), emote.zerovisual$pose()));
      if (emote.zerovisual$weight() > 0.0F) {
         state.field_55305.method_65605();
         state.field_55307.method_65605();
      }
   }
}
