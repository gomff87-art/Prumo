package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Animations;
import com.zerovisual.module.impl.visuals.ViewModel;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_759.class})
public class HeldItemRendererMixin {
   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")}
   )
   private void zerovisual$viewModelPush(
      class_742 player,
      float tickDelta,
      float pitch,
      class_1268 hand,
      float swingProgress,
      class_1799 item,
      float equipProgress,
      class_4587 matrices,
      class_4597 vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      matrices.method_22903();
      ViewModel viewModel = (ViewModel)ZeroVisualClient.get().modules().get(ViewModel.class);
      if (viewModel.isEnabled()) {
         class_1306 arm = hand == class_1268.field_5808 ? player.method_6068() : player.method_6068().method_5928();
         float side = arm == class_1306.field_6183 ? 1.0F : -1.0F;
         matrices.method_46416(viewModel.x.getFloat() * side, viewModel.y.getFloat(), viewModel.z.getFloat());
         float s = viewModel.scale.getFloat();
         matrices.method_22905(s, s, s);
      }
   }

   @Inject(
      method = {"swingArm"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$swingAnimation(float swingProgress, float equipProgress, class_4587 matrices, int armX, class_1306 arm, CallbackInfo ci) {
      Animations animations = (Animations)ZeroVisualClient.get().modules().get(Animations.class);
      if (animations.isEnabled() && !animations.mode.is("Vanilla")) {
         animations.applySwing(matrices, swingProgress, equipProgress, armX);
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("RETURN")}
   )
   private void zerovisual$viewModelPop(
      class_742 player,
      float tickDelta,
      float pitch,
      class_1268 hand,
      float swingProgress,
      class_1799 item,
      float equipProgress,
      class_4587 matrices,
      class_4597 vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      matrices.method_22909();
   }
}
