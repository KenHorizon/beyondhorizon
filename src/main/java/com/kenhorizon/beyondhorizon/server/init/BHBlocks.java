package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.block.WorkbenchBlock;
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
            .register("workbench", WorkbenchBlock::new)
            .inialProperties(() -> BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register()
            .build();

    public static final RegistryObject<Block> ADAMANTITE_ORE = RegistryBlocks
            .register("adamantite_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_ADAMANTITE, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> COBALT_ORE = RegistryBlocks
            .register("cobalt_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_COBALT, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> DEEPSLATE_COBALT_ORE = RegistryBlocks
            .register("deepslate_cobalt_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_COBALT, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = RegistryBlocks
            .register("deepslate_ruby_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_SILVER, 1, 3)
            .register()
            .build();
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = RegistryBlocks
            .register("deepslate_silver_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_SILVER, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> HALITE_ORE = RegistryBlocks
            .register("halite_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_SILVER, 1, 5)
            .register()
            .build();

    public static final RegistryObject<Block> HELLSTONE_ORE = RegistryBlocks
            .register("hellstone_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_HELLSTONE, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> NETHERRACK_HELLSTONE_ORE = RegistryBlocks
            .register("netherrack_hellstone_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_HELLSTONE, 1, 5)
            .register()
            .build();
    public static final RegistryObject<Block> PALLADIUM_ORE = RegistryBlocks
            .register("palladium_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_PALLADIUM, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> RUBY_ORE = RegistryBlocks
            .register("ruby_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_SILVER, 1, 3)
            .register()
            .build();
    public static final RegistryObject<Block> SILVER_ORE = RegistryBlocks
            .register("silver_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_SILVER, 1, 3)
            .register()
            .build();

    public static final RegistryObject<Block> SULFUR_ORE = RegistryBlocks
            .register("sulfur_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_ADAMANTITE, 1, 5)
            .register()
            .build();

    public static final RegistryObject<Block> TITANIUM_ORE = RegistryBlocks
            .register("titanium_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .oreDrop(BHItems.RAW_TITANIUM, 1, 3)
            .register()
            .build();


    public static void register(IEventBus eventBus) {
        RegistryEntries.BLOCKS.register(eventBus);
    }
}
