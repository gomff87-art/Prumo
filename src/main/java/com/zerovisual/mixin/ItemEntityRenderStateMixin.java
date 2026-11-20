package com.zerovisual.mixin;

import com.zerovisual.util.ItemPhysicsState;
import net.minecraft.class_10039;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_10039.class})
public class ItemEntityRenderStateMixin implements ItemPhysicsState {
   @Unique
   private boolean zerovisual$onGround;
   @Unique
   private float zerovisual$velocityX;
   @Unique
   private float zerovisual$velocityZ;

   public boolean zerovisual$isOnGround() {
      return this.zerovisual$onGround;
   }

   public void zerovisual$setOnGround(boolean onGround) {
      this.zerovisual$onGround = onGround;
   }

   public float zerovisual$velocityX() {
      return this.zerovisual$velocityX;
   }

   public float zerovisual$velocityZ() {
      return this.zerovisual$velocityZ;
   }

   public void zerovisual$setVelocity(float x, float z) {
      this.zerovisual$velocityX = x;
      this.zerovisual$velocityZ = z;
   }
}
