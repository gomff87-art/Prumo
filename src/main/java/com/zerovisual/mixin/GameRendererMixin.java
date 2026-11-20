package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.TotemPop;
import com.zerovisual.module.impl.visuals.AspectRatio;
import com.zerovisual.module.impl.visuals.MotionBlur;
import com.zerovisual.module.impl.visuals.NoHurtCam;
import com.zerovisual.module.impl.visuals.Zoom;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_9334;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_757.class})
public class GameRendererMixin {
   @ModifyReturnValue(
      method = {"getFov"},
      at = {@At("RETURN")}
   )
   private float zerovisual$zoom(float fov) {
      return fov / ((Zoom)ZeroVisualClient.get().modules().get(Zoom.class)).getDivisor();
   }

   @ModifyArg(
      method = {"getBasicProjectionMatrix"},
      at = {@At(
         value = "INVOKE",
         target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;",
         remap = false
      )},
      index = 1
   )
   private float zerovisual$aspectRatio(float aspect) {
      AspectRatio aspectRatio = (AspectRatio)ZeroVisualClient.get().modules().get(AspectRatio.class);
      return aspectRatio.isEnabled() ? aspectRatio.ratio.getFloat() : aspect;
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$noHurtCam(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      if (((NoHurtCam)ZeroVisualClient.get().modules().get(NoHurtCam.class)).isEnabled()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V",
         shift = Shift.AFTER
      )}
   )
   private void zerovisual$motionBlur(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
      MotionBlur motionBlur = (MotionBlur)ZeroVisualClient.get().modules().get(MotionBlur.class);
      if (motionBlur.isEnabled()) {
         motionBlur.apply();
      }
   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$totemPop(class_1799 stack, CallbackInfo ci) {
      if (stack.method_57826(class_9334.field_54274) && ((TotemPop)ZeroVisualClient.get().modules().get(TotemPop.class)).replacesFloatingItem()) {
         ci.cancel();
      }
   }
}
