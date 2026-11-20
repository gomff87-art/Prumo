package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.utilities.MainMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LogoRenderer.class})
public class LogoDrawerMixin {
   @Unique
   private static final Identifier ZEROVISUAL_LOGO = Identifier.fromNamespaceAndPath("zerovisual", "textures/gui/title_logo.png");
   @Shadow
   @Final
   private boolean field_41811;

   @Inject(
      method = {"draw(Lnet/minecraft/client/gui/DrawContext;IFI)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$logo(GuiGraphics context, int screenWidth, float alpha, int y, CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null && (Boolean)((MainMenu)client.modules().get(MainMenu.class)).customLogo.get()) {
         int color = ARGB.white(this.field_41811 ? 1.0F : alpha);
         context.method_25291(RenderType::method_62277, ZEROVISUAL_LOGO, screenWidth / 2 - 128, y - 4, 0.0F, 0.0F, 256, 64, 256, 64, color);
         ci.cancel();
      }
   }
}
