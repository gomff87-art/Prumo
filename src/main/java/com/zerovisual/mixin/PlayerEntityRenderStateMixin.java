package com.zerovisual.mixin;

import com.zerovisual.emote.EmotePose;
import com.zerovisual.util.EmoteState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({AvatarRenderState.class})
public class PlayerEntityRenderStateMixin implements EmoteState {
   @Unique
   private final EmotePose zerovisual$pose = new EmotePose();
   @Unique
   private float zerovisual$weight;

   public EmotePose zerovisual$pose() {
      return this.zerovisual$pose;
   }

   public float zerovisual$weight() {
      return this.zerovisual$weight;
   }

   public void zerovisual$setWeight(float weight) {
      this.zerovisual$weight = weight;
   }
}
