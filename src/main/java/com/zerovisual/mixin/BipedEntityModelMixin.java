package com.zerovisual.mixin;

import com.zerovisual.module.impl.cosmetics.Pet;
import com.zerovisual.util.EmoteRig;
import com.zerovisual.util.EmoteRigHolder;
import com.zerovisual.util.EmoteState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HumanoidModel.class})
public class BipedEntityModelMixin implements EmoteRigHolder {
   @Unique
   private EmoteRig zerovisual$rig;
   @Shadow
   @Final
   public ModelPart field_3398;
   @Shadow
   @Final
   public ModelPart field_3391;
   @Shadow
   @Final
   public ModelPart field_3401;
   @Shadow
   @Final
   public ModelPart field_27433;
   @Shadow
   @Final
   public ModelPart field_3392;
   @Shadow
   @Final
   public ModelPart field_3397;

   public EmoteRig zerovisual$rig() {
      return this.zerovisual$rig;
   }

   @Inject(
      method = {"setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"},
      at = {@At("TAIL")}
   )
   private void zerovisual$emote(HumanoidRenderState state, CallbackInfo ci) {
      if (state instanceof EmoteState emote) {
         float weight = emote.zerovisual$weight();
         if (weight > 0.0F && this.zerovisual$rig == null) {
            this.zerovisual$rig = new EmoteRig((HumanoidModel)this);
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
   private void zerovisual$emoteArmor(HumanoidModel<?> target, CallbackInfo ci) {
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
   private void zerovisual$miniMeIdle(HumanoidRenderState state, CallbackInfo ci) {
      if (state == Pet.posedState && !(Pet.poseBlend <= 0.001F)) {
         float k = Pet.poseBlend;
         float t = Pet.poseTime;
         switch (Pet.poseAnimation) {
            case 0:
               float swing = Mth.method_15374(t * 5.2F);
               this.field_3401.xRot += (swing * 0.75F - 0.15F) * k;
               this.field_27433.xRot += (swing * 0.75F - 0.15F) * k;
               this.field_3401.zRot = this.field_3401.zRot + (0.12F + Math.abs(swing) * 0.1F) * k;
               this.field_27433.zRot = this.field_27433.zRot - (0.12F + Math.abs(swing) * 0.1F) * k;
               this.field_3398.zRot = this.field_3398.zRot + Mth.method_15374(t * 5.2F) * 0.07F * k;
               this.field_3398.xRot = this.field_3398.xRot + Math.abs(Mth.method_15362(t * 5.2F)) * 0.06F * k;
               break;
            case 1:
               float beat = Math.max(0.0F, Mth.method_15374(t * 9.0F));
               this.field_3392.xRot += -beat * 0.32F * k;
               this.field_3392.zRot += 0.06F * k;
               this.field_3391.zRot = this.field_3391.zRot + Mth.method_15374(t * 4.5F) * 0.035F * k;
               this.field_3398.zRot = this.field_3398.zRot + Mth.method_15374(t * 4.5F) * 0.06F * k;
               this.field_3401.zRot += (0.1F + beat * 0.05F) * k;
               this.field_27433.zRot -= (0.1F + beat * 0.05F) * k;
               break;
            case 2:
               this.field_3401.xRot += 0.25F * k;
               this.field_27433.xRot += 0.25F * k;
               this.field_3392.yRot = this.field_3392.yRot + Mth.method_15374(t * 2.0F) * 0.08F * k;
               this.field_3397.yRot = this.field_3397.yRot - Mth.method_15374(t * 2.0F) * 0.08F * k;
         }
      }
   }
}
