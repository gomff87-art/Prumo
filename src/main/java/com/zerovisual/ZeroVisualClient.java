package com.zerovisual;

import com.zerovisual.account.AccountManager;
import com.zerovisual.config.ConfigManager;
import com.zerovisual.gui.AccountButton;
import com.zerovisual.gui.HudEditor;
import com.zerovisual.gui.PwfScreen;
import com.zerovisual.module.ModuleManager;
import com.zerovisual.module.impl.cosmetics.EmoteMenu;
import com.zerovisual.module.impl.cosmetics.FireFlies;
import com.zerovisual.module.impl.cosmetics.Halo;
import com.zerovisual.module.impl.cosmetics.HitParticles;
import com.zerovisual.module.impl.cosmetics.HitSound;
import com.zerovisual.module.impl.cosmetics.KillEffect;
import com.zerovisual.module.impl.cosmetics.Pet;
import com.zerovisual.module.impl.cosmetics.TotemPop;
import com.zerovisual.module.impl.cosmetics.Trails;
import com.zerovisual.module.impl.cosmetics.Wings;
import com.zerovisual.module.impl.hud.LowHealth;
import com.zerovisual.module.impl.utilities.AutoReconnect;
import com.zerovisual.module.impl.utilities.AutoTool;
import com.zerovisual.module.impl.utilities.MainMenu;
import com.zerovisual.module.impl.utilities.PlayWithFriends;
import com.zerovisual.module.impl.utilities.Waypoints;
import com.zerovisual.module.impl.visuals.ChinaHat;
import com.zerovisual.module.impl.visuals.DamageNumbers;
import com.zerovisual.module.impl.visuals.Freelook;
import com.zerovisual.module.impl.visuals.HitboxColor;
import com.zerovisual.module.impl.visuals.JumpCircles;
import com.zerovisual.module.impl.visuals.Nametags;
import com.zerovisual.module.impl.visuals.Seasons;
import com.zerovisual.module.impl.visuals.TargetEsp;
import com.zerovisual.module.impl.visuals.Zoom;
import com.zerovisual.pwf.PwfManager;
import com.zerovisual.util.BuildVerifier;
import com.zerovisual.util.DevScreenshots;
import com.zerovisual.util.TargetTracker;
import com.zerovisual.waypoint.WaypointManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.AfterEntities;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BlockOutline;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.End;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterInit;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AfterRender;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.class_1269;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_4185;
import net.minecraft.class_433;
import net.minecraft.class_442;
import net.minecraft.class_500;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeroVisualClient implements ClientModInitializer {
   public static final String MOD_ID = "zerovisual";
   public static final String NAME = "prumo visuals";
   public static final Logger LOGGER = LoggerFactory.getLogger("prumo visuals");
   private static ZeroVisualClient instance;
   private ModuleManager modules;
   private ConfigManager config;
   private AccountManager accounts;
   private WaypointManager waypoints;

   public void onInitializeClient() {
      instance = this;
      BuildVerifier.start();
      Zoom.registerKeyBinding();
      EmoteMenu.registerKeyBinding();
      Freelook.registerKeyBinding();
      Waypoints.registerKeyBinding();
      this.modules = new ModuleManager();
      this.modules.init();
      this.config = new ConfigManager(this.modules);
      this.config.load();
      this.accounts = new AccountManager();
      this.accounts.load();
      this.waypoints = new WaypointManager();
      this.waypoints.load();
      ClientPlayConnectionEvents.JOIN.register((Join)(handler, sender, client) -> ((AutoReconnect)this.modules.get(AutoReconnect.class)).onJoin());
      HudEditor.register();
      DevScreenshots.registerIfRequested();
      ScreenEvents.AFTER_INIT
         .register(
            (AfterInit)(client, screen, scaledWidth, scaledHeight) -> {
               if (screen instanceof class_442 && (Boolean)((MainMenu)this.modules.get(MainMenu.class)).accountButton.get()) {
                  Screens.getButtons(screen).add(new AccountButton(screen, 6, 6));
               }

               if (AutoReconnect.isDisconnectScreen(screen)) {
                  ((AutoReconnect)this.modules.get(AutoReconnect.class)).onDisconnectedScreen(screen);
               }

               if (((PlayWithFriends)this.modules.get(PlayWithFriends.class)).isEnabled()) {
                  if (screen instanceof class_433 && client.method_1496()) {
                     Screens.getButtons(screen)
                        .add(
                           class_4185.method_46430(
                                 class_2561.method_48321("zerovisual.pwf.button_host", "Play with friends"), b -> client.method_1507(PwfScreen.host(screen))
                              )
                              .method_46434(scaledWidth - 136, 6, 130, 20)
                              .method_46431()
                        );
                  }

                  if (screen instanceof class_500) {
                     Screens.getButtons(screen)
                        .add(
                           class_4185.method_46430(
                                 class_2561.method_48321("zerovisual.pwf.button_join", "Join a friend"), b -> client.method_1507(PwfScreen.join(screen))
                              )
                              .method_46434(scaledWidth - 136, 6, 130, 20)
                              .method_46431()
                        );
                  }
               }

               if (screen instanceof class_442) {
                  ScreenEvents.afterRender(screen)
                     .register((AfterRender)(s2, ctx, mouseX, mouseY, delta) -> BuildVerifier.renderTitleWarning(ctx, s2.field_22789, s2.field_22790));
               }
            }
         );
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> this.modules.onTick());
      ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(handler, client) -> PwfManager.closeHosted());
      HudRenderCallback.EVENT.register((HudRenderCallback)(ctx, tickCounter) -> {
         class_310 mc = class_310.method_1551();
         if (!mc.field_1690.field_1842 && !mc.field_1705.method_53531().method_53536()) {
            ctx.method_51448().method_22903();
            ctx.method_51448().method_46416(0.0F, 0.0F, 200.0F);
            this.modules.renderHud(ctx, tickCounter.method_60637(false));
            AutoTool autoTool = (AutoTool)this.modules.get(AutoTool.class);
            if (autoTool.isEnabled()) {
               autoTool.render(ctx);
            }

            BuildVerifier.renderHudMark(ctx);
            LowHealth lowHealth = (LowHealth)this.modules.get(LowHealth.class);
            if (lowHealth.isEnabled()) {
               lowHealth.render(ctx);
            }

            TotemPop totemPop = (TotemPop)this.modules.get(TotemPop.class);
            if (totemPop.isEnabled()) {
               totemPop.renderHud(ctx);
            }

            ctx.method_51448().method_22909();
         }
      });
      WorldRenderEvents.AFTER_ENTITIES.register((AfterEntities)context -> {
         JumpCircles jumpCircles = (JumpCircles)this.modules.get(JumpCircles.class);
         if (jumpCircles.isEnabled()) {
            jumpCircles.render(context);
         }

         Pet pet = (Pet)this.modules.get(Pet.class);
         if (pet.isEnabled()) {
            pet.render(context);
         }

         TotemPop totemPopWorld = (TotemPop)this.modules.get(TotemPop.class);
         if (totemPopWorld.isEnabled()) {
            totemPopWorld.renderWorld(context);
         }

         ChinaHat chinaHat = (ChinaHat)this.modules.get(ChinaHat.class);
         if (chinaHat.isEnabled()) {
            chinaHat.render(context);
         }

         HitParticles hitParticles = (HitParticles)this.modules.get(HitParticles.class);
         if (hitParticles.isEnabled()) {
            hitParticles.render(context);
         }

         Trails trails = (Trails)this.modules.get(Trails.class);
         if (trails.isEnabled()) {
            trails.render(context);
         }

         TargetEsp targetEsp = (TargetEsp)this.modules.get(TargetEsp.class);
         if (targetEsp.isEnabled()) {
            targetEsp.render(context);
         }

         HitboxColor hitboxColor = (HitboxColor)this.modules.get(HitboxColor.class);
         if (hitboxColor.isEnabled()) {
            hitboxColor.render(context);
         }

         FireFlies fireFlies = (FireFlies)this.modules.get(FireFlies.class);
         if (fireFlies.isEnabled()) {
            fireFlies.render(context);
         }

         Seasons seasons = (Seasons)this.modules.get(Seasons.class);
         if (seasons.isEnabled()) {
            seasons.render(context);
         }

         Halo halo = (Halo)this.modules.get(Halo.class);
         if (halo.isEnabled()) {
            halo.render(context);
         }

         KillEffect killEffect = (KillEffect)this.modules.get(KillEffect.class);
         if (killEffect.isEnabled()) {
            killEffect.render(context);
         }

         DamageNumbers damageNumbers = (DamageNumbers)this.modules.get(DamageNumbers.class);
         if (damageNumbers.isEnabled()) {
            damageNumbers.render(context);
         }

         Wings wings = (Wings)this.modules.get(Wings.class);
         if (wings.isEnabled()) {
            wings.render(context);
         }
      });
      WorldRenderEvents.END.register((End)context -> {
         Nametags nametags = (Nametags)this.modules.get(Nametags.class);
         if (nametags.isEnabled()) {
            nametags.render(context);
         }

         Waypoints waypointsModule = (Waypoints)this.modules.get(Waypoints.class);
         if (waypointsModule.isEnabled()) {
            waypointsModule.render(context);
         }
      });
      WorldRenderEvents.BLOCK_OUTLINE
         .register(
            (BlockOutline)(context, outline) -> {
               com.zerovisual.module.impl.visuals.BlockOutline blockOutline = (com.zerovisual.module.impl.visuals.BlockOutline)this.modules
                  .get(com.zerovisual.module.impl.visuals.BlockOutline.class);
               return !blockOutline.isEnabled() || blockOutline.render(context, outline);
            }
         );
      AttackEntityCallback.EVENT.register((AttackEntityCallback)(player, world, hand, entity, hitResult) -> {
         if (world.field_9236) {
            TargetTracker.onAttack(entity);
            HitParticles hitParticles = (HitParticles)this.modules.get(HitParticles.class);
            if (hitParticles.isEnabled()) {
               hitParticles.spawn(entity);
            }

            HitSound hitSound = (HitSound)this.modules.get(HitSound.class);
            if (hitSound.isEnabled()) {
               hitSound.onAttack(entity);
            }
         }

         return class_1269.field_5811;
      });
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)client -> this.config.save());
      LOGGER.info("{} initialized with {} modules", "prumo visuals", this.modules.getModules().size());
   }

   public static ZeroVisualClient get() {
      return instance;
   }

   public ModuleManager modules() {
      return this.modules;
   }

   public ConfigManager config() {
      return this.config;
   }

   public AccountManager accounts() {
      return this.accounts;
   }

   public WaypointManager waypoints() {
      return this.waypoints;
   }
}
