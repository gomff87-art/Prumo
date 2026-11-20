package com.zerovisual;

import java.lang.reflect.Method;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class ExportHud implements ClientModInitializer {
   private static Method isExporting;
   private static boolean looked;

   private static boolean exporting() {
      if (!looked) {
         looked = true;

         try {
            isExporting = Class.forName("com.moulberry.flashback.Flashback").getMethod("isExporting");
         } catch (Throwable var2) {
            isExporting = null;
         }
      }

      if (isExporting == null) {
         return false;
      } else {
         try {
            return Boolean.TRUE.equals(isExporting.invoke(null));
         } catch (Throwable var1) {
            return false;
         }
      }
   }

   public void onInitializeClient() {
      HudRenderCallback.EVENT.register(ExportHud::draw);
   }

   private static void draw(GuiGraphics var0, DeltaTracker var1) {
      try {
         if (!exporting()) {
            return;
         }

         Minecraft var2 = Minecraft.getInstance();
         if (var2 == null || var2.options == null || var2.gui == null) {
            return;
         }

         boolean var3 = var2.options.hideGui || var2.gui.getDebugOverlay().showDebugScreen();
         if (!var3) {
            return;
         }

         ZeroVisualClient var4 = ZeroVisualClient.get();
         if (var4 == null) {
            return;
         }

         var0.method_51448().pushPose();
         var0.method_51448().translate(0.0F, 0.0F, 200.0F);

         try {
            var4.modules().renderHud(var0, var1.getGameTimeDeltaPartialTick(false));
         } finally {
            var0.method_51448().popPose();
         }
      } catch (Throwable var9) {
      }
   }
}
