package com.zerovisual.mixin;

import com.zerovisual.ZeroVisualClient;
import com.zerovisual.module.ModuleManager;
import com.zerovisual.module.impl.visuals.ItemZoom;
import com.zerovisual.module.impl.visuals.SmoothSwap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractContainerScreen.class})
public abstract class HandledScreenMixin<T extends AbstractContainerMenu> {
   @Shadow
   protected Slot field_2787;
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
   private void zerovisual$swapUpdate(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
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
   private void zerovisual$swapRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
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
   private void zerovisual$slotBegin(GuiGraphics context, Slot slot, CallbackInfo ci) {
      ModuleManager modules = ZeroVisualClient.get().modules();
      SmoothSwap swap = (SmoothSwap)modules.get(SmoothSwap.class);
      if (swap.isEnabled() && swap.hides(slot)) {
         ci.cancel();
      } else {
         context.method_51448().pushPose();
         ItemZoom zoom = (ItemZoom)modules.get(ItemZoom.class);
         if (zoom.isEnabled()) {
            float s = zoom.scaleFor(slot, slot == this.field_2787 && slot.hasItem());
            if (!(s <= 1.001F)) {
               context.method_51448().translate((float)(slot.x + 8), (float)(slot.y + 8), 50.0F);
               context.method_51448().scale(s, s, 1.0F);
               context.method_51448().translate((float)(-(slot.x + 8)), (float)(-(slot.y + 8)), 0.0F);
            }
         }
      }
   }

   @Inject(
      method = {"drawSlot"},
      at = {@At("RETURN")}
   )
   private void zerovisual$slotEnd(GuiGraphics context, Slot slot, CallbackInfo ci) {
      context.method_51448().popPose();
   }
}
