package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.ModuleManager;
import com.zerovisual.module.impl.visuals.ItemZoom;
import com.zerovisual.module.impl.visuals.SmoothSwap;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_332;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_465.class})
public abstract class HandledScreenMixin<T extends class_1703> {
   @Shadow
   protected class_1735 field_2787;
   @Shadow
   @Final
   protected T field_2797;

   @Inject(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlots(Lnet/minecraft/client/gui/DrawContext;)V"
      )}
   )
   private void zerovisual$swapUpdate(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      SmoothSwap swap = (SmoothSwap)ZeroVisualClient.get().modules().get(SmoothSwap.class);
      if (swap.isEnabled()) {
         swap.update(this.field_2797);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlots(Lnet/minecraft/client/gui/DrawContext;)V",
         shift = Shift.AFTER
      )}
   )
   private void zerovisual$swapRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      SmoothSwap swap = (SmoothSwap)ZeroVisualClient.get().modules().get(SmoothSwap.class);
      if (swap.isEnabled()) {
         swap.render(context);
      }
   }

   @Inject(
      method = {"drawSlot"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void zerovisual$slotBegin(class_332 context, class_1735 slot, CallbackInfo ci) {
      ModuleManager modules = ZeroVisualClient.get().modules();
      SmoothSwap swap = (SmoothSwap)modules.get(SmoothSwap.class);
      if (swap.isEnabled() && swap.hides(slot)) {
         ci.cancel();
      } else {
         context.method_51448().method_22903();
         ItemZoom zoom = (ItemZoom)modules.get(ItemZoom.class);
         if (zoom.isEnabled()) {
            float s = zoom.scaleFor(slot, slot == this.field_2787 && slot.method_7681());
            if (!(s <= 1.001F)) {
               context.method_51448().method_46416((float)(slot.field_7873 + 8), (float)(slot.field_7872 + 8), 50.0F);
               context.method_51448().method_22905(s, s, 1.0F);
               context.method_51448().method_46416((float)(-(slot.field_7873 + 8)), (float)(-(slot.field_7872 + 8)), 0.0F);
            }
         }
      }
   }

   @Inject(
      method = {"drawSlot"},
      at = {@At("RETURN")}
   )
   private void zerovisual$slotEnd(class_332 context, class_1735 slot, CallbackInfo ci) {
      context.method_51448().method_22909();
   }
}
