package com.kenhorizon.beyondhorizon.mixins.server.accessor;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Attribute.class)
public interface AttributeAccessor {
    @Accessor("syncable")
    @Mutable
    void setSyncable(boolean setSyncable);
}
