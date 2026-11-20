package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.CinematicCamera;
import com.zerovisual.module.impl.visuals.Zoom;
import com.zerovisual.util.CpsCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MouseHandler.class})
public class MouseMixin {
   @Inject(
      method = {"onMouseButton"},
      at = {@At("HEAD")}
   )
   private void zerovisual$countClicks(long window, int button, int action, int mods, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (action == 1 && window == mc.getWindow().handle() && mc.screen == null) {
         CpsCounter.click(button);
      }
   }

   @Inject(
      method = {"onMouseScroll"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$zoomScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (window == mc.getWindow().handle() && mc.screen == null) {
         boolean discrete = (Boolean)mc.options.discreteMouseScroll().get();
         double amount = (discrete ? Math.signum(vertical) : vertical) * (Double)mc.options.mouseWheelSensitivity().get();
         if (((Zoom)ZeroVisualClient.get().modules().get(Zoom.class)).onScroll(amount)) {
            ci.cancel();
         }
      }
   }

   @ModifyArg(
      method = {"updateMouse"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/Smoother;smooth(DD)D"
      )},
      index = 1
   )
   private double zerovisual$cinematic(double time) {
      ZeroVisualClient client = ZeroVisualClient.get();
      return client == null ? time : ((CinematicCamera)client.modules().get(CinematicCamera.class)).smoothingTime(time);
   }
}
