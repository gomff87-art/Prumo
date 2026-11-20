package com.zerovisual.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zerovisual.gui.ButtonStyle;
import java.util.function.Function;
import net.minecraft.class_1921;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_4264;
import net.minecraft.class_8662;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_4264.class})
public abstract class PressableWidgetMixin extends class_339 {
   public PressableWidgetMixin(int x, int y, int width, int height, class_2561 message) {
      super(x, y, width, height, message);
   }

   @WrapOperation(
      method = {"renderWidget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIIII)V"
      )}
   )
   private void zerovisual$background(
      class_332 context, Function<class_2960, class_1921> layers, class_2960 sprite, int x, int y, int width, int height, int color, Operation<Void> original
   ) {
      if (!ButtonStyle.isEnabled()) {
         original.call(new Object[]{context, layers, sprite, x, y, width, height, color});
      } else {
         ButtonStyle.drawButton(context, this, (float)x, (float)y, (float)width, (float)height, this.field_22763, this.method_25367(), this.field_22765);
      }
   }

   @WrapOperation(
      method = {"renderWidget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/widget/PressableWidget;drawMessage(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;I)V"
      )}
   )
   private void zerovisual$label(class_4264 widget, class_332 context, class_327 textRenderer, int color, Operation<Void> original) {
      if (ButtonStyle.isEnabled() && !(widget instanceof class_8662)) {
         ButtonStyle.drawLabel(
            context,
            this,
            this.method_25369(),
            (float)this.method_46426(),
            (float)this.method_46427(),
            (float)this.method_25368(),
            (float)this.method_25364(),
            this.field_22763,
            this.method_25367(),
            this.field_22765
         );
      } else {
         original.call(new Object[]{widget, context, textRenderer, color});
      }
   }
}
