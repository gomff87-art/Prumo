package com.zerovisual.mixin;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ModelPart.class})
public interface ModelPartAccessor {
   @Accessor("cuboids")
   List<Cube> zerovisual$getCuboids();

   @Mutable
   @Accessor("cuboids")
   void zerovisual$setCuboids(List<Cube> var1);

   @Accessor("children")
   Map<String, ModelPart> zerovisual$getChildren();

   @Mutable
   @Accessor("children")
   void zerovisual$setChildren(Map<String, ModelPart> var1);
}
