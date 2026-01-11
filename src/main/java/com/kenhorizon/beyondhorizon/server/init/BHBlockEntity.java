package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.block.entity.BaseSpawnerBlockEntity;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BHBlockEntity {

    public static final RegistryObject<BlockEntityType<BaseSpawnerBlockEntity>> BASE_SPAWNER = registerTileEntity("base_spawner_entity", () -> BlockEntityType.Builder.of(BaseSpawnerBlockEntity::new, BHBlocks.SPAWNER.get()));
    public static final RegistryObject<BlockEntityType<BaseSpawnerBlockEntity>> BASE_SPAWNER_OMINOUS = registerTileEntity("base_spawner_ominous_entity", () -> BlockEntityType.Builder.of(BaseSpawnerBlockEntity::new, BHBlocks.SPAWNER.get()));

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerTileEntity(String name, Supplier<BlockEntityType.Builder<T>> supplier) {
        return RegistryEntries.BLOCK_ENTITIES.register(name, () -> supplier.get().build(null));
    }

    public static void register(IEventBus eventBus) {
        RegistryEntries.BLOCK_ENTITIES.register(eventBus);
    }
}
