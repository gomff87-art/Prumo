package com.zerovisual.mixin;

import com.zerovisual.gui.ClickGuiScreen;
import com.zerovisual.gui.GlassPicker;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClickGuiScreen.class})
public abstract class GlassClickGuiMixin extends class_437 {
   protected GlassClickGuiMixin(class_2561 var1) {
      super(var1);
   }

   @Inject(
      method = {"method_25394"},
      at = {@At("RETURN")}
   )
   private void zerovisual$glassPicker(class_332 var1, int var2, int var3, float var4, CallbackInfo var5) {
      GlassPicker.render(var1, var2, var3, this.field_22789, this.field_22790);
   }

   @Inject(
      method = {"method_25426"},
      at = {@At("RETURN")}
   )
   private void zerovisual$glassReset(CallbackInfo var1) {
      GlassPicker.reset();
   }

   @Inject(
      method = {"method_25402"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$glassClick(double var1, double var3, int var5, CallbackInfoReturnable<Boolean> var6) {
      if (GlassPicker.click(var1, var3, var5)) {
         var6.setReturnValue(true);
      }
   }
}
