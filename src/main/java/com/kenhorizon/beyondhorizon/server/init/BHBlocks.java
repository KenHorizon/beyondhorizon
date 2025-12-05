package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.datagen.BHBlockTagsProvider;
import com.kenhorizon.libs.registry.RegistryBlocks;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHBlocks {

    public static final RegistryObject<Block> WORKBENCH = RegistryBlocks
            .register("workbench", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register()
            .build();

    public static final RegistryObject<Block> COBALT_ORE = RegistryBlocks
            .register("cobalt_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register()
            .build();

    public static void register(IEventBus eventBus) {
        RegistryEntries.BLOCKS.register(eventBus);
    }
}
