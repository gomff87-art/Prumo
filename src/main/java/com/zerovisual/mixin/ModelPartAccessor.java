package com.zerovisual.mixin;

import java.util.List;
import java.util.Map;
import net.minecraft.class_630;
import net.minecraft.class_630.class_628;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_630.class})
public interface ModelPartAccessor {
   @Accessor("cuboids")
   List<class_628> zerovisual$getCuboids();

   @Mutable
   @Accessor("cuboids")
   void zerovisual$setCuboids(List<class_628> var1);

   @Accessor("children")
   Map<String, class_630> zerovisual$getChildren();

   @Mutable
   @Accessor("children")
   void zerovisual$setChildren(Map<String, class_630> var1);
}
