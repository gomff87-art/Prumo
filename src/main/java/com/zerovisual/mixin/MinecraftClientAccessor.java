package com.zerovisual.mixin;

import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_7853;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_310.class})
public interface MinecraftClientAccessor {
   @Mutable
   @Accessor("session")
   void zerovisual$setSession(class_320 var1);

   @Mutable
   @Accessor("profileKeys")
   void zerovisual$setProfileKeys(class_7853 var1);
}
