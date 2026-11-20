package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.CinematicCamera;
import com.zerovisual.module.impl.visuals.Zoom;
import com.zerovisual.util.CpsCounter;
import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_312.class})
public class MouseMixin {
   @Inject(
      method = {"onMouseButton"},
      at = {@At("HEAD")}
   )
   private void zerovisual$countClicks(long window, int button, int action, int mods, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      if (action == 1 && window == mc.method_22683().method_4490() && mc.field_1755 == null) {
         CpsCounter.click(button);
      }
   }

   @Inject(
      method = {"onMouseScroll"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$zoomScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      if (window == mc.method_22683().method_4490() && mc.field_1755 == null) {
         boolean discrete = (Boolean)mc.field_1690.method_42439().method_41753();
         double amount = (discrete ? Math.signum(vertical) : vertical) * (Double)mc.field_1690.method_41806().method_41753();
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
