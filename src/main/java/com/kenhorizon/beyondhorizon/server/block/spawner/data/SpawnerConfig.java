package com.kenhorizon.beyondhorizon.server.block.spawner.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public record SpawnerConfig(int requiredPlayerRange, int spawnRange, float totalMobs, float simultaneousMobs, float totalMobsAddedPerPlayer, float simultaneousMobsAddedPerPlayer, int ticksBetweenSpawn, int targetCooldownLength, SimpleWeightedRandomList<SpawnData> spawnPotentialsDefinition, SimpleWeightedRandomList<ResourceLocation> lootTablesToEject) {
    public static SpawnerConfig DEFAULT = new SpawnerConfig(
            14,
            4,
            6.0F,
            4.0F,
            2.0F,
            1.0F,
            40,
            36000,
            SimpleWeightedRandomList.empty(),
            SimpleWeightedRandomList.<ResourceLocation>builder().add(BuiltInLootTables.ANCIENT_CITY, 1).add(BuiltInLootTables.BASTION_TREASURE, 1).build()
    );

    public static final String REQUIRED_PLAYER_RANGE = "required_player_range";
    public static final String SPAWN_RANGE = "spawn_range";
    public static final String TOTAL_MOBS = "total_mobs";
    public static final String SIMULTANEOUS_MOBS = "simultaneous_mobs";
    public static final String TOTAL_MOB_ADDED_PER_PLAYER = "total_mobs_added_per_player";
    public static final String MOB_ADDED_PER_PLAYER = "simultaneous_mobs_added_per_player";
    public static final String DELAY = "ticks_between_spawn";
    public static final String COOLDOWN = "target_cooldown_length";
    public static final String SPAWN_POTENTIALS = "spawn_potentials";
    public static final String LOOT_TABLET_TO_EJECT = "loot_tables_to_eject";
    public static MapCodec<SpawnerConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.intRange(1, 128).optionalFieldOf(REQUIRED_PLAYER_RANGE, DEFAULT.requiredPlayerRange).forGetter(SpawnerConfig::requiredPlayerRange),
                            Codec.intRange(1, 128).optionalFieldOf(SPAWN_RANGE, DEFAULT.spawnRange).forGetter(SpawnerConfig::spawnRange),
                            Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf(TOTAL_MOBS, DEFAULT.totalMobs).forGetter(SpawnerConfig::totalMobs),
                            Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf(SIMULTANEOUS_MOBS, DEFAULT.simultaneousMobs).forGetter(SpawnerConfig::simultaneousMobs),
                            Codec.floatRange(0.0F, Float.MAX_VALUE)
                                    .optionalFieldOf(TOTAL_MOB_ADDED_PER_PLAYER, DEFAULT.totalMobsAddedPerPlayer)
                                    .forGetter(SpawnerConfig::totalMobsAddedPerPlayer),
                            Codec.floatRange(0.0F, Float.MAX_VALUE)
                                    .optionalFieldOf(MOB_ADDED_PER_PLAYER, DEFAULT.simultaneousMobsAddedPerPlayer)
                                    .forGetter(SpawnerConfig::simultaneousMobsAddedPerPlayer),
                            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf(DELAY, DEFAULT.ticksBetweenSpawn).forGetter(SpawnerConfig::ticksBetweenSpawn),
                            Codec.intRange(0, Integer.MAX_VALUE)
                                    .optionalFieldOf(COOLDOWN, DEFAULT.targetCooldownLength)
                                    .forGetter(SpawnerConfig::targetCooldownLength),
                            SpawnData.LIST_CODEC.optionalFieldOf(SPAWN_POTENTIALS, SimpleWeightedRandomList.empty()).forGetter(SpawnerConfig::spawnPotentialsDefinition),
                            SimpleWeightedRandomList.wrappedCodecAllowingEmpty(ResourceLocation.CODEC)
                                    .optionalFieldOf(LOOT_TABLET_TO_EJECT, SimpleWeightedRandomList.empty())
                                    .forGetter(SpawnerConfig::lootTablesToEject)
                    ).apply(instance, SpawnerConfig::new)
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int spawnRange = 4;
        private float totalMobs = 6.0F;
        private float simultaneousMobs = 2.0F;
        private float totalMobsAddedPerPlayer = 2.0F;
        private float simultaneousMobsAddedPerPlayer = 1.0F;
        private int ticksBetweenSpawn = 40;
        private SimpleWeightedRandomList<SpawnData> spawnPotentialsDefinition = SimpleWeightedRandomList.empty();
        private SimpleWeightedRandomList<ResourceLocation> lootTablesToEject;

        public Builder() {
            this.lootTablesToEject = SimpleWeightedRandomList.empty();
        }

        public Builder spawnRange(final int spawnRange) {
            this.spawnRange = spawnRange;
            return this;
        }

        public Builder totalMobs(final float totalMobs) {
            this.totalMobs = totalMobs;
            return this;
        }

        public Builder simultaneousMobs(final float simultaneousMobs) {
            this.simultaneousMobs = simultaneousMobs;
            return this;
        }

        public Builder totalMobsAddedPerPlayer(final float totalMobsAddedPerPlayer) {
            this.totalMobsAddedPerPlayer = totalMobsAddedPerPlayer;
            return this;
        }

        public Builder simultaneousMobsAddedPerPlayer(final float simultaneousMobsAddedPerPlayer) {
            this.simultaneousMobsAddedPerPlayer = simultaneousMobsAddedPerPlayer;
            return this;
        }

        public Builder ticksBetweenSpawn(final int ticksBetweenSpawn) {
            this.ticksBetweenSpawn = ticksBetweenSpawn;
            return this;
        }

        public Builder spawnPotentialsDefinition(final SimpleWeightedRandomList<SpawnData> spawnPotentialsDefinition) {
            this.spawnPotentialsDefinition = spawnPotentialsDefinition;
            return this;
        }

        public Builder lootTablesToEject(final SimpleWeightedRandomList<ResourceLocation> lootTablesToEject) {
            this.lootTablesToEject = lootTablesToEject;
            return this;
        }

        public SpawnerConfig build() {
            return new SpawnerConfig(
                    14,
                    this.spawnRange,
                    this.totalMobs,
                    this.simultaneousMobs,
                    this.totalMobsAddedPerPlayer,
                    this.simultaneousMobsAddedPerPlayer,
                    this.ticksBetweenSpawn,
                    36000,
                    this.spawnPotentialsDefinition,
                    this.lootTablesToEject
            );
        }
    }
}
