package com.kenhorizon.beyondhorizon.server.listeners;

import com.google.gson.*;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerConfig;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.HashMap;
import java.util.Map;

public class SpawnerBuilderListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final String DIRECTORY = "base_spawner";
    public static final Map<ResourceLocation, SpawnerConfig> BUILDERS = new HashMap<>();

    public SpawnerBuilderListener(ICondition.IContext context) {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> maps, ResourceManager manager, ProfilerFiller profilerFiller) {
        BUILDERS.clear();
        maps.forEach((id, jsonElement) -> {
            SpawnerConfig.MAP_CODEC.codec()
                    .decode(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(error -> BeyondHorizon.LOGGER.error("Failed to load spawner {}: {}", id, error))
                    .ifPresent(spawnerBuilder -> {
                        BUILDERS.put(id, spawnerBuilder.getFirst());
                        BeyondHorizon.LOGGER.debug("Data-driven Spawners: {}", BUILDERS);
                    });
        });
    }

    public static SpawnerConfig get(ResourceLocation id) {
        return BUILDERS.get(id);
    }
}
