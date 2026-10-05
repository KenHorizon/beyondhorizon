package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;

public class BHStructureTags {

    public static final TagKey<Structure> ON_SEALED_RELIC_CRYPT = create("on_sealed_relic_crypt");

    public static TagKey<Structure> create(String name) {
        return TagKey.create(Registries.STRUCTURE, BeyondHorizon.resource(name));
    }
}
