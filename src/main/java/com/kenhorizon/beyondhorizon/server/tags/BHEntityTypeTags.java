package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class BHEntityTypeTags {
//    public static final TagKey<EntityType<?>> UNAFFECTED_VOID_MATTERS = create("unaffected_void_matters");

    public static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, BeyondHorizon.resource(name));
    }
}