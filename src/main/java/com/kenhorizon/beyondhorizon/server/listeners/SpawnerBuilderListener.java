package com.kenhorizon.beyondhorizon.server.listeners;

import com.google.gson.*;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerBuilder;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.SpawnData;

import java.util.HashMap;
import java.util.Map;

public class SpawnerBuilderListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final String DIRECTORY = "base_spawner";
    public static final Map<ResourceLocation, SpawnerBuilder> BUILDERS = new HashMap<>();

    public SpawnerBuilderListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> maps, ResourceManager manager, ProfilerFiller profilerFiller) {
        BUILDERS.clear();
        for (int i = 0; i < maps.size(); i++) {
            ResourceLocation id = (ResourceLocation) maps.keySet().toArray()[i];
            JsonObject json = maps.get(id).getAsJsonObject();
            int requiredRange = json.getAsJsonPrimitive(SpawnerBuilder.REQUIRED_PLAYER_RANGE).getAsInt();
            int spawnRange = json.getAsJsonPrimitive(SpawnerBuilder.SPAWN_RANGE).getAsInt();
            int totalMobs = json.getAsJsonPrimitive(SpawnerBuilder.TOTAL_MOBS).getAsInt();
            int simulatneousMobs = json.getAsJsonPrimitive(SpawnerBuilder.SIMULTANEOUS_MOBS).getAsInt();
            int totalMobAddedPerPlayer = json.getAsJsonPrimitive(SpawnerBuilder.TOTAL_MOB_ADDED_PER_PLAYER).getAsInt();
            int simultaneousMobsAddedPerPlayer = json.getAsJsonPrimitive(SpawnerBuilder.MOB_ADDED_PER_PLAYER).getAsInt();
            int tickBetweenSpawn = json.getAsJsonPrimitive(SpawnerBuilder.DELAY).getAsInt();
            int targetCooldown = json.getAsJsonPrimitive(SpawnerBuilder.COOLDOWN).getAsInt();
            SimpleWeightedRandomList.Builder<ResourceLocation> lootBuilder = SimpleWeightedRandomList.builder();
            SimpleWeightedRandomList.Builder<SpawnData> spawnBuilder = SimpleWeightedRandomList.builder();

            JsonArray spawnArray = json.getAsJsonArray(SpawnerBuilder.SPAWN_POTENTIALS);
            JsonArray lootArray = json.getAsJsonArray(SpawnerBuilder.LOOT_TABLET_TO_EJECT);
            for (JsonElement element : lootArray) {
                JsonObject obj = element.getAsJsonObject();
                ResourceLocation table = ResourceLocation.parse(obj.get("data").getAsString());
                int weight = obj.get("weight").getAsInt();
                lootBuilder.add(table, weight);
            }
            for (JsonElement element : spawnArray) {
                JsonObject obj = element.getAsJsonObject();

                int weight = obj.get("weight").getAsInt();
                JsonObject data = obj.getAsJsonObject("data");

                SpawnData spawnData = SpawnData.CODEC.parse(JsonOps.INSTANCE, data).getOrThrow(false, BeyondHorizon.LOGGER::error);

                spawnBuilder.add(spawnData, weight);
            }
            SimpleWeightedRandomList<SpawnData> spawnMobs = spawnBuilder.build();
            SimpleWeightedRandomList<ResourceLocation> lootToEject = lootBuilder.build();
            BUILDERS.put(id, new SpawnerBuilder(requiredRange, spawnRange, totalMobs, simulatneousMobs, totalMobAddedPerPlayer, simultaneousMobsAddedPerPlayer, tickBetweenSpawn, targetCooldown, spawnMobs, lootToEject));
        }
//        maps.forEach((id, jsonElement) -> {
//            SpawnerBuilder.MAP_CODEC
//                    .codec()
//                    .decode(JsonOps.INSTANCE, jsonElement)
//                    .resultOrPartial(error -> BeyondHorizon.LOGGER.error("Failed to load spawner {}: {}", id, error))
//                    .ifPresent(spawnerBuilder -> {
//                        BUILDERS.put(id, spawnerBuilder.getFirst());
//                    });
//        });
    }
}
