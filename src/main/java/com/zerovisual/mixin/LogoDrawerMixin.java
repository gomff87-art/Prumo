package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.utilities.MainMenu;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_8020;
import net.minecraft.class_9848;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_8020.class})
public class LogoDrawerMixin {
   @Unique
   private static final class_2960 ZEROVISUAL_LOGO = class_2960.method_60655("zerovisual", "textures/gui/title_logo.png");
   @Shadow
   @Final
   private boolean field_41811;

   @Inject(
      method = {"draw(Lnet/minecraft/client/gui/DrawContext;IFI)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$logo(class_332 context, int screenWidth, float alpha, int y, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null && (Boolean)((MainMenu)client.modules().get(MainMenu.class)).customLogo.get()) {
         int color = class_9848.method_61317(this.field_41811 ? 1.0F : alpha);
         context.method_25291(class_1921::method_62277, ZEROVISUAL_LOGO, screenWidth / 2 - 128, y - 4, 0.0F, 0.0F, 256, 64, 256, 64, color);
         ci.cancel();
      }
   }
}
