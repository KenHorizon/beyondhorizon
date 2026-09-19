package com.kenhorizon.beyondhorizon.server.block.spawner.data;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHLootTables;
import com.kenhorizon.beyondhorizon.server.level.entity.EquipmentTable;
import com.kenhorizon.beyondhorizon.server.level.SpawnerSpawnData;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.util.WeightListed;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class SpawnerConfigs {
    private static final Keys TESTING_PHASE = SpawnerConfigs.Keys.of("spawner/testing_phase");
    private static final Keys SEALED_RUNIC_CRYPT_LEFT = SpawnerConfigs.Keys.of("spawner/sealed_runic_crypt_left");
    private static final Keys SEALED_RUNIC_CRYPT_RIGHT = SpawnerConfigs.Keys.of("spawner/sealed_runic_crypt_right");
    private static final Keys SEALED_RUNIC_CRYPT_MIDDLE = SpawnerConfigs.Keys.of("spawner/sealed_runic_crypt_middle");

    public static void bootstrap(BootstapContext<SpawnerConfig> context) {
        BeyondHorizon.LOGGER.debug("Registering Spawner Builder...");
        register(context, TESTING_PHASE, SpawnerConfig.builder()
                .simultaneousMobs(1.0F)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(20).totalMobs(2.0F)
                .spawnPotentialsDefinition(WeightListed.of(
                        spawnData(EntityType.HUSK)
                ))
                .lootTablesToEject(SimpleWeightedRandomList.<ResourceLocation>builder()
                        .add(BuiltInLootTables.ANCIENT_CITY, 3)
                        .add(BuiltInLootTables.ABANDONED_MINESHAFT, 7)
                        .build())
                .build());
        register(context, SEALED_RUNIC_CRYPT_LEFT, SpawnerConfig.builder()
                .simultaneousMobs(1.0F)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(20).totalMobs(3.0F)
                .spawnPotentialsDefinition(WeightListed.of(spawnDataWithEquipment(EntityType.ZOMBIE, BHLootTables.COMMON_EQUIPMENTS)))
                .lootTablesToEject(SimpleWeightedRandomList.<ResourceLocation>builder()
                        .add(BHLootTables.SEALED_RUNIC_CRYPT_LEFT, 3)
                        .build())
                .build());
        register(context, SEALED_RUNIC_CRYPT_RIGHT, SpawnerConfig.builder()
                .simultaneousMobs(1.0F)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(20).totalMobs(3.0F)
                .spawnPotentialsDefinition(WeightListed.of(spawnDataWithEquipment(EntityType.ZOMBIE, BHLootTables.COMMON_EQUIPMENTS)))
                .lootTablesToEject(SimpleWeightedRandomList.<ResourceLocation>builder()
                        .add(BHLootTables.SEALED_RUNIC_CRYPT_RIGHT, 3)
                        .build())
                .build());
        register(context, SEALED_RUNIC_CRYPT_MIDDLE, SpawnerConfig.builder()
                .simultaneousMobs(1.0F)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(20).totalMobs(3.0F)
                .spawnPotentialsDefinition(WeightListed.of(spawnDataWithEquipment(EntityType.ZOMBIE, BHLootTables.COMMON_EQUIPMENTS)))
                .lootTablesToEject(SimpleWeightedRandomList.<ResourceLocation>builder()
                        .add(BHLootTables.SEALED_RUNIC_CRYPT_MIDDLE, 3)
                        .build())
                .build());
    }

    private static <T extends Entity> SpawnerSpawnData spawnData(final EntityType<T> type) {
        return customSpawnDataWithEquipment(type, (tag) -> {
        }, (ResourceLocation) null);
    }
    private static <T extends Entity> SpawnerSpawnData customSpawnData(final EntityType<T> type, final Consumer<CompoundTag> tagModifier) {
        return customSpawnDataWithEquipment(type, tagModifier, (ResourceLocation)null);
    }

    private static <T extends Entity> SpawnerSpawnData spawnDataWithEquipment(final EntityType<T> type, final ResourceLocation equipmentLootTable) {
        return customSpawnDataWithEquipment(type, (tag) -> {
        }, equipmentLootTable);
    }

    private static <T extends Entity> SpawnerSpawnData customSpawnDataWithEquipment(final EntityType<T> type, final Consumer<CompoundTag> tagModifier, final @Nullable ResourceLocation equipmentLootTable) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
        Optional<EquipmentTable> table = Optional.ofNullable(equipmentLootTable).map(loottable -> new EquipmentTable(loottable, 0.0F));
        tagModifier.accept(tag);
        return new SpawnerSpawnData(tag, Optional.empty(), table);
    }

    private static void register(BootstapContext<SpawnerConfig> context, final Keys keys, final SpawnerConfig builder) {
        context.register(keys.register(), builder);
    }

    private static ResourceKey<SpawnerConfig> registryKey(final String id) {
        return ResourceKey.create(BHRegistries.Keys.SPAWNER_BUILDER, BeyondHorizon.resource(id));
    }

    private static record Keys(ResourceKey<SpawnerConfig> register) {
        public static Keys of(final String id) {
            return new Keys(SpawnerConfigs.registryKey(id));
        }
    }
}
