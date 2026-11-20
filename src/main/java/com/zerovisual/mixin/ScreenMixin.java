package com.zerovisual.mixin;

import com.zerovisual.render.MenuBackground;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
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
   private void zerovisual$starBackground(GuiGraphics context, float delta, CallbackInfo ci) {
      if (MenuBackground.isEnabled()) {
         MenuBackground.render(context, this.field_22789, this.field_22790, 1.0F);
         ci.cancel();
      }
   }
}
