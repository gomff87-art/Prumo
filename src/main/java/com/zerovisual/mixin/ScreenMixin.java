package com.zerovisual.mixin;

import com.zerovisual.render.MenuBackground;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_437.class})
public class ScreenMixin {
   @Shadow
   public int field_22789;
   @Shadow
   public int field_22790;

   @Inject(
      method = {"renderPanoramaBackground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$starBackground(class_332 context, float delta, CallbackInfo ci) {
      if (MenuBackground.isEnabled()) {
         MenuBackground.render(context, this.field_22789, this.field_22790, 1.0F);
         ci.cancel();
      }
   }
}
