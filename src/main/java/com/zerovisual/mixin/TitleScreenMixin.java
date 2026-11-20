package com.zerovisual.mixin;

import com.zerovisual.render.MenuBackground;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_442.class})
public abstract class TitleScreenMixin extends class_437 {
   @Shadow
   private float field_49514;

   protected TitleScreenMixin(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"renderPanoramaBackground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$starBackground(class_332 context, float delta, CallbackInfo ci) {
      if (MenuBackground.isEnabled()) {
         MenuBackground.render(context, this.field_22789, this.field_22790, this.field_49514);
         ci.cancel();
      }
   }
}
