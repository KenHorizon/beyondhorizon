package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public interface BHStructures {

    ResourceKey<Structure> RELIC_ARENA = createKey("relic_arena");

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, BeyondHorizon.resource(name));
    }
}
