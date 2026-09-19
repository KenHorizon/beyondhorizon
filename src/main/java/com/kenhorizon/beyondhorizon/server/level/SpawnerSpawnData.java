package com.kenhorizon.beyondhorizon.server.level;

import com.kenhorizon.beyondhorizon.server.level.entity.EquipmentTable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.random.SimpleWeightedRandomList;

import java.util.Optional;

public record SpawnerSpawnData(CompoundTag entityToSpawn, Optional<SpawnerSpawnData.CustomSpawnRules> customSpawnRules, Optional<EquipmentTable> equipment) {
    public static final String ENTITY_TAG = "entity";
    public static final Codec<SpawnerSpawnData> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(CompoundTag.CODEC.fieldOf("entity").forGetter((o) -> {
            return o.entityToSpawn;
        }), SpawnerSpawnData.CustomSpawnRules.CODEC.optionalFieldOf("custom_spawn_rules").forGetter((o) -> {
            return o.customSpawnRules;
        }), EquipmentTable.CODEC.optionalFieldOf("equipment").forGetter((o) -> o.equipment)).apply(instance, SpawnerSpawnData::new);
    });
    public static final Codec<SimpleWeightedRandomList<SpawnerSpawnData>> LIST_CODEC = SimpleWeightedRandomList.wrappedCodecAllowingEmpty(CODEC);

    public SpawnerSpawnData() {
        this(new CompoundTag(), Optional.empty(), Optional.empty());
    }

    public SpawnerSpawnData {
        if (entityToSpawn.contains("id")) {
            ResourceLocation resourcelocation = ResourceLocation.tryParse(entityToSpawn.getString("id"));
            if (resourcelocation != null) {
                entityToSpawn.putString("id", resourcelocation.toString());
            } else {
                entityToSpawn.remove("id");
            }
        }

    }

    public CompoundTag getEntityToSpawn() {
        return this.entityToSpawn;
    }

    public Optional<SpawnerSpawnData.CustomSpawnRules> getCustomSpawnRules() {
        return this.customSpawnRules;
    }

    public Optional<EquipmentTable> getEquipmentTable() {
        return this.equipment;
    }

    public static record CustomSpawnRules(InclusiveRange<Integer> blockLightLimit, InclusiveRange<Integer> skyLightLimit) {
        private static final InclusiveRange<Integer> LIGHT_RANGE = new InclusiveRange<>(0, 15);
        public static final Codec<SpawnerSpawnData.CustomSpawnRules> CODEC = RecordCodecBuilder.create((p_286217_) -> {
            return p_286217_.group(lightLimit("block_light_limit").forGetter((p_186600_) -> {
                return p_186600_.blockLightLimit;
            }), lightLimit("sky_light_limit").forGetter((p_186595_) -> {
                return p_186595_.skyLightLimit;
            })).apply(p_286217_, SpawnerSpawnData.CustomSpawnRules::new);
        });

        private static DataResult<InclusiveRange<Integer>> checkLightBoundaries(InclusiveRange<Integer> p_186593_) {
            return !LIGHT_RANGE.contains(p_186593_) ? DataResult.error(() -> {
                return "Light values must be withing range " + LIGHT_RANGE;
            }) : DataResult.success(p_186593_);
        }

        private static MapCodec<InclusiveRange<Integer>> lightLimit(String p_286409_) {
            return ExtraCodecs.validate(InclusiveRange.INT.optionalFieldOf(p_286409_, LIGHT_RANGE), SpawnerSpawnData.CustomSpawnRules::checkLightBoundaries);
        }
    }
}