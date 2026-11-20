package com.zerovisual.mixin;

import com.zerovisual.util.EmoteRig;
import com.zerovisual.util.EmoteRigHolder;
import net.minecraft.class_1306;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_591.class})
public class PlayerEntityModelMixin {
   @Inject(
      method = {"setArmAngle"},
      at = {@At("TAIL")}
   )
   private void zerovisual$toForearm(class_1306 arm, class_4587 matrices, CallbackInfo ci) {
      EmoteRig rig = ((EmoteRigHolder)this).zerovisual$rig();
      if (rig != null) {
         rig.toHand(arm, matrices);
      }
   }
}
