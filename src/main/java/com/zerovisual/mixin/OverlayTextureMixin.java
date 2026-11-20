package com.zerovisual.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.HitColor;
import com.zerovisual.module.impl.visuals.ShieldColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({OverlayTexture.class})
public class OverlayTextureMixin {
   @Shadow
   @Final
   private DynamicTexture field_21013;
   @Unique
   private int zerovisual$uploadedColor = -1275133952;
   @Unique
   private int zerovisual$uploadedReady = -1275133952;
   @Unique
   private int zerovisual$uploadedBroken = -1275133952;

   @Inject(
      method = {"setupOverlayColor"},
      at = {@At("HEAD")}
   )
   private void zerovisual$applyHitColor(CallbackInfo ci) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client != null) {
         int color = ((HitColor)client.modules().get(HitColor.class)).getOverlayColor();
         ShieldColor shield = (ShieldColor)client.modules().get(ShieldColor.class);
         int ready = shield.rowColor(false);
         int broken = shield.rowColor(true);
         if (color != this.zerovisual$uploadedColor || ready != this.zerovisual$uploadedReady || broken != this.zerovisual$uploadedBroken) {
            this.zerovisual$uploadedColor = color;
            this.zerovisual$uploadedReady = ready;
            this.zerovisual$uploadedBroken = broken;
            NativeImage image = this.field_21013.getPixels();
            if (image != null) {
               for (int u = 0; u < 16; u++) {
                  image.setPixel(u, 0, ready);
                  image.setPixel(u, 1, broken);

                  for (int v = 2; v < 8; v++) {
                     image.setPixel(u, v, color);
                  }
               }

               RenderSystem.activeTexture(33985);
               this.field_21013.method_23207();
               image.method_22619(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), false);
               RenderSystem.activeTexture(33984);
            }
         }
      }
   }
}
