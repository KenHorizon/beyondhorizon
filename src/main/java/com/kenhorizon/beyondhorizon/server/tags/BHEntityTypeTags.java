package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class BHEntityTypeTags {
    public static final TagKey<EntityType<?>> VOID_BANE_AFFECTED = create("void_bane_affected");
    public static final TagKey<EntityType<?>> UNAFFECTED_BY_LEVELS = create("unaffected_by_levels");

    public static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, BeyondHorizon.resource(name));
    }
}