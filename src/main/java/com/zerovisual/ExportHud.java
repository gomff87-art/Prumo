package com.zerovisual;

import java.lang.reflect.Method;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_9779;

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

   private static void draw(class_332 var0, class_9779 var1) {
      try {
         if (!exporting()) {
            return;
         }

         class_310 var2 = class_310.method_1551();
         if (var2 == null || var2.field_1690 == null || var2.field_1705 == null) {
            return;
         }

         boolean var3 = var2.field_1690.field_1842 || var2.field_1705.method_53531().method_53536();
         if (!var3) {
            return;
         }

         ZeroVisualClient var4 = ZeroVisualClient.get();
         if (var4 == null) {
            return;
         }

         var0.method_51448().method_22903();
         var0.method_51448().method_46416(0.0F, 0.0F, 200.0F);

         try {
            var4.modules().renderHud(var0, var1.method_60637(false));
         } finally {
            var0.method_51448().method_22909();
         }
      } catch (Throwable var9) {
      }
   }
}
