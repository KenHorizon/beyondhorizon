package com.kenhorizon.beyondhorizon.server.block.spawner.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public record SpawnerBuilder(
        int requiredPlayerRange,
        int spawnRange,
        float totalMobs,
        float simultaneousMobs,
        float totalMobsAddedPerPlayer,
        float simultaneousMobsAddedPerPlayer,
        int ticksBetweenSpawn,
        int targetCooldownLength,
        SimpleWeightedRandomList<SpawnData> spawnPotentialsDefinition,
        SimpleWeightedRandomList<ResourceLocation> lootTablesToEject
) {
    public static SpawnerBuilder DEFAULT = new SpawnerBuilder(
            14,
            4,
            6.0F,
            2.0F,
            2.0F,
            1.0F,
            40,
            36000,
            SimpleWeightedRandomList.empty(),
            SimpleWeightedRandomList.<ResourceLocation>builder().add(BuiltInLootTables.ANCIENT_CITY, 1).add(BuiltInLootTables.BASTION_TREASURE, 1).build()
    );
    public static MapCodec<SpawnerBuilder> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.intRange(1, 128).optionalFieldOf("required_player_range", DEFAULT.requiredPlayerRange).forGetter(SpawnerBuilder::requiredPlayerRange),
                            Codec.intRange(1, 128).optionalFieldOf("spawn_range", DEFAULT.spawnRange).forGetter(SpawnerBuilder::spawnRange),
                            Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf("total_mobs", DEFAULT.totalMobs).forGetter(SpawnerBuilder::totalMobs),
                            Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf("simultaneous_mobs", DEFAULT.simultaneousMobs).forGetter(SpawnerBuilder::simultaneousMobs),
                            Codec.floatRange(0.0F, Float.MAX_VALUE)
                                    .optionalFieldOf("total_mobs_added_per_player", DEFAULT.totalMobsAddedPerPlayer)
                                    .forGetter(SpawnerBuilder::totalMobsAddedPerPlayer),
                            Codec.floatRange(0.0F, Float.MAX_VALUE)
                                    .optionalFieldOf("simultaneous_mobs_added_per_player", DEFAULT.simultaneousMobsAddedPerPlayer)
                                    .forGetter(SpawnerBuilder::simultaneousMobsAddedPerPlayer),
                            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("ticks_between_spawn", DEFAULT.ticksBetweenSpawn).forGetter(SpawnerBuilder::ticksBetweenSpawn),
                            Codec.intRange(0, Integer.MAX_VALUE)
                                    .optionalFieldOf("target_cooldown_length", DEFAULT.targetCooldownLength)
                                    .forGetter(SpawnerBuilder::targetCooldownLength),
                            SpawnData.LIST_CODEC.optionalFieldOf("spawn_potentials", SimpleWeightedRandomList.empty()).forGetter(SpawnerBuilder::spawnPotentialsDefinition),
                            SimpleWeightedRandomList.wrappedCodecAllowingEmpty(ResourceLocation.CODEC)
                                    .optionalFieldOf("loot_tables_to_eject", SimpleWeightedRandomList.empty())
                                    .forGetter(SpawnerBuilder::lootTablesToEject)
                    )
                    .apply(instance, SpawnerBuilder::new)
    );
    public int calculateTargetTotalMobs(int total) {
        return (int)Math.floor(this.totalMobs + this.totalMobsAddedPerPlayer * total);
    }

    public int calculateTargetSimultaneousMobs(int total) {
        return (int)Math.floor(this.simultaneousMobs + this.simultaneousMobsAddedPerPlayer * total);
    }

    @Override
    public int requiredPlayerRange() {
        return requiredPlayerRange;
    }

    @Override
    public int targetCooldownLength() {
        return targetCooldownLength;
    }
}
