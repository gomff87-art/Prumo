package com.zerovisual.mixin;

import com.zerovisual.render.MenuBackground;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public abstract class TitleScreenMixin extends Screen {
   @Shadow
   private float field_49514;

   protected TitleScreenMixin(Component title) {
      super(title);
   }

   @Inject(
      method = {"renderPanoramaBackground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$starBackground(GuiGraphics context, float delta, CallbackInfo ci) {
      if (MenuBackground.isEnabled()) {
         MenuBackground.render(context, this.width, this.height, this.field_49514);
         ci.cancel();
      }
   }
}
