package com.zerovisual.mixin;

import com.zerovisual.module.impl.cosmetics.Pet;
import com.zerovisual.util.EmoteRig;
import com.zerovisual.util.EmoteRigHolder;
import com.zerovisual.util.EmoteState;
import net.minecraft.class_10034;
import net.minecraft.class_3532;
import net.minecraft.class_572;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_572.class})
public class BipedEntityModelMixin implements EmoteRigHolder {
   @Unique
   private EmoteRig zerovisual$rig;
   @Shadow
   @Final
   public class_630 field_3398;
   @Shadow
   @Final
   public class_630 field_3391;
   @Shadow
   @Final
   public class_630 field_3401;
   @Shadow
   @Final
   public class_630 field_27433;
   @Shadow
   @Final
   public class_630 field_3392;
   @Shadow
   @Final
   public class_630 field_3397;

   public EmoteRig zerovisual$rig() {
      return this.zerovisual$rig;
   }

   @Inject(
      method = {"setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$emote(class_10034 state, CallbackInfo ci) {
      if (state instanceof EmoteState emote) {
         float weight = emote.zerovisual$weight();
         if (weight > 0.0F && this.zerovisual$rig == null) {
            this.zerovisual$rig = new EmoteRig((class_572)this);
         }

         if (this.zerovisual$rig != null) {
            this.zerovisual$rig.rest();
            if (weight > 0.0F) {
               this.zerovisual$rig.apply(emote.zerovisual$pose(), weight);
            }
         }
      }
   }

   @Inject(
      method = {"copyTransforms"},
      at = {@At("TAIL")}
   )
   private void zerovisual$emoteArmor(class_572<?> target, CallbackInfo ci) {
      if (this.zerovisual$rig != null) {
         EmoteRigHolder holder = (EmoteRigHolder)target;
         if (holder.zerovisual$rig() == null) {
            ((BipedEntityModelMixin)target).zerovisual$rig = new EmoteRig(target);
         }

         holder.zerovisual$rig().copyFrom(this.zerovisual$rig);
      }
   }

   @Inject(
      method = {"setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$miniMeIdle(class_10034 state, CallbackInfo ci) {
      if (state == Pet.posedState && !(Pet.poseBlend <= 0.001F)) {
         float k = Pet.poseBlend;
         float t = Pet.poseTime;
         switch (Pet.poseAnimation) {
            case 0:
               float swing = class_3532.method_15374(t * 5.2F);
               this.field_3401.field_3654 += (swing * 0.75F - 0.15F) * k;
               this.field_27433.field_3654 += (swing * 0.75F - 0.15F) * k;
               this.field_3401.field_3674 = this.field_3401.field_3674 + (0.12F + Math.abs(swing) * 0.1F) * k;
               this.field_27433.field_3674 = this.field_27433.field_3674 - (0.12F + Math.abs(swing) * 0.1F) * k;
               this.field_3398.field_3674 = this.field_3398.field_3674 + class_3532.method_15374(t * 5.2F) * 0.07F * k;
               this.field_3398.field_3654 = this.field_3398.field_3654 + Math.abs(class_3532.method_15362(t * 5.2F)) * 0.06F * k;
               break;
            case 1:
               float beat = Math.max(0.0F, class_3532.method_15374(t * 9.0F));
               this.field_3392.field_3654 += -beat * 0.32F * k;
               this.field_3392.field_3674 += 0.06F * k;
               this.field_3391.field_3674 = this.field_3391.field_3674 + class_3532.method_15374(t * 4.5F) * 0.035F * k;
               this.field_3398.field_3674 = this.field_3398.field_3674 + class_3532.method_15374(t * 4.5F) * 0.06F * k;
               this.field_3401.field_3674 += (0.1F + beat * 0.05F) * k;
               this.field_27433.field_3674 -= (0.1F + beat * 0.05F) * k;
               break;
            case 2:
               this.field_3401.field_3654 += 0.25F * k;
               this.field_27433.field_3654 += 0.25F * k;
               this.field_3392.field_3675 = this.field_3392.field_3675 + class_3532.method_15374(t * 2.0F) * 0.08F * k;
               this.field_3397.field_3675 = this.field_3397.field_3675 - class_3532.method_15374(t * 2.0F) * 0.08F * k;
         }
      }
   }
}
