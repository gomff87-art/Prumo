package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.cosmetics.TotemPop;
import com.zerovisual.module.impl.visuals.AspectRatio;
import com.zerovisual.module.impl.visuals.MotionBlur;
import com.zerovisual.module.impl.visuals.NoHurtCam;
import com.zerovisual.module.impl.visuals.Zoom;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
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
   private void zerovisual$noHurtCam(PoseStack matrices, float tickDelta, CallbackInfo ci) {
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
   private void zerovisual$motionBlur(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
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
   private void zerovisual$totemPop(ItemStack stack, CallbackInfo ci) {
      if (stack.has(DataComponents.DEATH_PROTECTION) && ((TotemPop)ZeroVisualClient.get().modules().get(TotemPop.class)).replacesFloatingItem()) {
         ci.cancel();
      }
   }
}
