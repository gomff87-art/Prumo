package com.zerovisual.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zerovisual.util.EmoteRig;
import com.zerovisual.util.EmoteRigHolder;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerModel.class})
public class PlayerEntityModelMixin {
   @Inject(
      method = {"setArmAngle"},
      at = {@At("TAIL")}
   )
   private void zerovisual$toForearm(HumanoidArm arm, PoseStack matrices, CallbackInfo ci) {
      EmoteRig rig = ((EmoteRigHolder)this).zerovisual$rig();
      if (rig != null) {
         rig.toHand(arm, matrices);
      }
   }
}
