package com.zerovisual;

import com.zerovisual.gui.Theme;
import com.zerovisual.render.ColorUtil;
import com.zerovisual.render.Render2D;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

public class IntroHook implements ClientModInitializer {
   private static final String NAME = "PRUMO VISUAL";
   private static final long TOTAL = 3600L;
   private static boolean attached;
   private static long startedAt;
   private static boolean finished;

   public void onInitializeClient() {
      ScreenEvents.AFTER_INIT.register((AfterInit)(var0, var1, var2, var3) -> {
         if (!finished && !attached && var1 instanceof TitleScreen) {
            attached = true;
            startedAt = System.currentTimeMillis();
            ScreenEvents.afterRender(var1).register(IntroHook::draw);
         }
      });
   }

   private static float clamp(float var0) {
      return var0 < 0.0F ? 0.0F : (var0 > 1.0F ? 1.0F : var0);
   }

   private static float easeOut(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   private static void draw(Screen var0, GuiGraphics var1, int var2, int var3, float var4) {
      if (!finished) {
         try {
            long var5 = System.currentTimeMillis() - startedAt;
            if (var5 >= 3600L) {
               finished = true;
               return;
            }

            int var7 = var0.width;
            int var8 = var0.height;
            float var9 = 1.0F - clamp((float)(var5 - 2900L) / 700.0F);
            float var10 = clamp((float)var5 / 250.0F);
            float var11 = Math.min(var10, var9);
            var1.fill(0, 0, var7, var8, ColorUtil.withAlpha(-16119278, var11));
            int var12 = Theme.accent();
            int var13 = Theme.accentLight();
            float var14 = (float)var7 / 2.0F;
            float var15 = (float)var8 / 2.0F;
            float var16 = 3.0F;
            float var17 = 0.0F;
            float[] var18 = new float["PRUMO VISUAL".length()];

            for (int var19 = 0; var19 < "PRUMO VISUAL".length(); var19++) {
               var18[var19] = (float)Render2D.textWidth(String.valueOf("PRUMO VISUAL".charAt(var19)));
               if ("PRUMO VISUAL".charAt(var19) == ' ') {
                  var18[var19] = 5.0F;
               }

               var17 += (var18[var19] + 1.0F) * var16;
            }

            float var30 = var14 - var17 / 2.0F;
            float var20 = var15 - 14.0F;

            for (int var21 = 0; var21 < "PRUMO VISUAL".length(); var21++) {
               char var22 = "PRUMO VISUAL".charAt(var21);
               float var23 = clamp((float)(var5 - 250L - (long)var21 * 85L) / 520.0F);
               float var24 = easeOut(var23);
               if (var22 != ' ' && var23 > 0.0F) {
                  float var25 = (1.0F - var24) * 34.0F;
                  int var26 = ColorUtil.lerp(var13, -1, var24);
                  Render2D.scaledText(var1, String.valueOf(var22), var30, var20 + var25, var16, ColorUtil.withAlpha(var26, var24 * var11));
               }

               var30 += (var18[var21] + 1.0F) * var16;
            }

            float var31 = 0.5F + 0.5F * (float)Math.sin((double)var5 / 260.0);
            float var32 = easeOut(clamp((float)(var5 - 100L) / 600.0F));
            float var33 = 26.0F * var32;
            if (var33 > 1.0F) {
               Render2D.gradientV(
                  var1, var14 - var33 / 2.0F, var20 - 52.0F, var33, var33, 7.0F, ColorUtil.withAlpha(var13, var11), ColorUtil.withAlpha(var12, var11)
               );
               Render2D.outline(
                  var1,
                  var14 - var33 / 2.0F - 2.0F,
                  var20 - 54.0F,
                  var33 + 4.0F,
                  var33 + 4.0F,
                  8.0F,
                  1.0F,
                  ColorUtil.withAlpha(var13, (0.2F + 0.6F * var31) * var11)
               );
            }

            float var34 = 170.0F;
            float var35 = var14 - var34 / 2.0F;
            float var36 = var20 + 44.0F;
            float var27 = easeOut(clamp(((float)var5 - 300.0F) / 2600.0F));
            Render2D.rect(var1, var35, var36, var34, 3.0F, 1.5F, ColorUtil.withAlpha(-14013898, var11));
            if (var27 > 0.01F) {
               Render2D.gradientH(var1, var35, var36, var34 * var27, 3.0F, 1.5F, ColorUtil.withAlpha(var12, var11), ColorUtil.withAlpha(var13, var11));
            }

            String var28 = var27 < 0.35F ? "Loading modules..." : (var27 < 0.8F ? "Building interface..." : "Ready");
            Render2D.centeredText(var1, var28, var14, var36 + 9.0F, ColorUtil.withAlpha(-6645072, var11));
         } catch (Throwable var29) {
            finished = true;
         }
      }
   }
}
