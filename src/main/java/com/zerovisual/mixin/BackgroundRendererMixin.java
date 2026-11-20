package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.impl.visuals.Ambience;
import com.zerovisual.module.impl.visuals.CustomSky;
import net.minecraft.class_310;
import net.minecraft.class_758;
import net.minecraft.class_5294.class_5401;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_758.class})
public class BackgroundRendererMixin {
   @ModifyReturnValue(
      method = {"getFogColor"},
      at = {@At("RETURN")}
   )
   private static Vector4f zerovisual$fogColor(Vector4f color) {
      ZeroVisualClient client = ZeroVisualClient.get();
      if (client == null) {
         return color;
      } else {
         CustomSky sky = (CustomSky)client.modules().get(CustomSky.class);
         if (sky.isActive()
            && !((Ambience)client.modules().get(Ambience.class)).isEnabled()
            && class_310.method_1551().field_1687.method_28103().method_29992() == class_5401.field_25640) {
            float[] fog = sky.fogColor();
            return new Vector4f(fog[0], fog[1], fog[2], color.w);
         } else {
            Ambience ambience = (Ambience)client.modules().get(Ambience.class);
            if (!ambience.isEnabled()) {
               return color;
            } else {
               float[] tinted = ambience.tintFog(color.x, color.y, color.z);
               return new Vector4f(tinted[0], tinted[1], tinted[2], color.w);
            }
         }
      }
   }
}
