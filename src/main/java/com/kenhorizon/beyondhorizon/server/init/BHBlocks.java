package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.block.ChainPulleyBlock;
import com.kenhorizon.beyondhorizon.server.block.GateBlocks;
import com.kenhorizon.beyondhorizon.server.block.WorkbenchBlock;
import com.kenhorizon.beyondhorizon.server.block.basin.FireBasinBlock;
import com.kenhorizon.beyondhorizon.server.block.basin.WallFireBasinBlock;
import com.kenhorizon.beyondhorizon.server.block.spawner.BaseSpawnerBlock;
import com.kenhorizon.libs.registry.RegistryBlocks;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHBlocks {

    public static final BlockBehaviour.Properties NETHER_BRICKS = BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS);
    public static final BlockBehaviour.Properties SPAWNER_PROPERTIES = BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).lightLevel(value -> value.getValue(BaseSpawnerBlock.SPAWNER_STATE).lightLevel()).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(50.0F).sound(BHSoundType.SPAWNER).noOcclusion().isViewBlocking(BHBlocks::never);

    public static final RegistryObject<Block> GATE = RegistryBlocks
            .register("gate", properties -> new GateBlocks(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> GATE_PARTS = RegistryBlocks
            .register("gate_parts", properties -> new GateBlocks.GateParts(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(-1.0F, 3600000.0F).noLootTable().isValidSpawn(BHBlocks::never)))
            .dontCreateItemBlocks()
            .register();


    public static final RegistryObject<Block> SPAWNER = RegistryBlocks
            .register("base_spawner", properties -> new BaseSpawnerBlock(SPAWNER_PROPERTIES))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> DUNGEON_BRICKS = RegistryBlocks
            .register("dungeon_bricks", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> CHAIN_PULLEY = RegistryBlocks
            .register("chain_pulley", properties -> new ChainPulleyBlock(BlockBehaviour.Properties.copy(Blocks.GRINDSTONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> WORKBENCH = RegistryBlocks
            .register("workbench", properties -> new WorkbenchBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> SILTSTONE = RegistryBlocks
            .register("siltstone", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> SILTSTONE_SLAB = RegistryBlocks
            .register("siltstone_slab", properties -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> SILTSTONE_STAIRS = RegistryBlocks
            .register("siltstone_stair", properties -> new StairBlock(() -> BHBlocks.SILTSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> SILTSTONE_BRICKS = RegistryBlocks
            .register("siltstone_bricks", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> SILTSTONE_BRICK_SLAB = RegistryBlocks
            .register("siltstone_brick_slab", properties -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> SILTSTONE_BRICK_STAIRS = RegistryBlocks
            .register("siltstone_brick_stairs", properties -> new StairBlock(() -> BHBlocks.SILTSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> WALL_FIRE_BASIN = RegistryBlocks
            .register("wall_fire_basin", properties -> new WallFireBasinBlock(NETHER_BRICKS))
            .properties(p -> p.lightLevel(value -> { return 8; }))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .dontCreateItemBlocks()
            .register();

    public static final RegistryObject<Block> FIRE_BASIN = RegistryBlocks
            .register("fire_basin", properties -> new FireBasinBlock(NETHER_BRICKS))
            .properties(p -> p.lightLevel(value -> { return 8; }))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .standWall(BHBlocks.WALL_FIRE_BASIN)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> CUT_NETHER_BRICKS = RegistryBlocks
            .register("cut_nether_bricks", properties -> new RotatedPillarBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> NETHER_PILLAR = RegistryBlocks
            .register("nether_pillar", properties -> new RotatedPillarBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> NETHER_BRICK_LANTERN = RegistryBlocks
            .register("nether_brick_lantern", Block::new)
            .properties(p -> p.lightLevel(value -> { return 15; }))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> POLISHED_NETHER = RegistryBlocks
            .register("polished_nether", properties -> new Block(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> POLISHED_NETHER_SLAB = RegistryBlocks
            .register("polished_nether_slab", properties -> new SlabBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> POLISHED_NETHER_STAIR = RegistryBlocks
            .register("polished_nether_stair", properties -> new StairBlock(() -> BHBlocks.POLISHED_NETHER.get().defaultBlockState(), NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> BLACK_NETHER_BRICKS = RegistryBlocks
            .register("black_nether_bricks", properties -> new RotatedPillarBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> BLACK_NETHER_BRICK_SLAB = RegistryBlocks
            .register("black_nether_brick_slab", properties -> new SlabBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> BLACK_NETHER_BRICK_STAIR = RegistryBlocks
            .register("black_nether_brick_stair", properties -> new StairBlock(() -> BHBlocks.BLACK_NETHER_BRICKS.get().defaultBlockState(), NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> CUT_BLACK_NETHER_BRICKS = RegistryBlocks
            .register("cut_black_nether_bricks", properties -> new RotatedPillarBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> BLACK_NETHER_PILLAR = RegistryBlocks
            .register("black_nether_pillar", properties -> new RotatedPillarBlock(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> POLISHED_BLACK_NETHER = RegistryBlocks
            .register("polished_black_nether", properties -> new Block(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> CHISELLED_BLACK_NETHER_BRICKS = RegistryBlocks
            .register("chiselled_black_nether_bricks", properties -> new Block(NETHER_BRICKS))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> VOIDSTONE = RegistryBlocks
            .register("voidstone", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.DIAMOND)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> RADIANCE_CRYSTRAL = RegistryBlocks
            .register("radiance_crystal", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.VERDANT_FROGLIGHT)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.STONE)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> DESOLATE_OBSIDIAN = RegistryBlocks
            .register("desolate_obsidian", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> DESOLATE_OBSIDIAN_BRICKS = RegistryBlocks
            .register("desolate_obsidian_bricks", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> DESOLATE_OBSIDIAN_BRICK_SLAB = RegistryBlocks
            .register("desolate_obsidian_brick_slab", properties -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> DESOLATE_OBSIDIAN_BRICK_STAIR = RegistryBlocks
            .register("desolate_obsidian_brick_stair", properties -> new StairBlock(() -> BHBlocks.DESOLATE_OBSIDIAN_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> PURPUR_END_STONE_PILLAR = RegistryBlocks
            .register("purpur_end_stone_brick_pillar", properties -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_GREY_STONE = RegistryBlocks
            .register("end_grey_stone", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_GREY_PILLAR = RegistryBlocks
            .register("end_grey_pillar", properties -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_GREY_BRICKS = RegistryBlocks
            .register("end_grey_bricks", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_GREY_BRICK_SLAB = RegistryBlocks
            .register("end_grey_brick_slab", properties -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_GREY_BRICK_STAIRS = RegistryBlocks
            .register("end_grey_brick_stair", properties -> new StairBlock(() -> BHBlocks.END_GREY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_STONE_TILES = RegistryBlocks
            .register("end_stone_tiles", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> END_STONE_PILLAR = RegistryBlocks
            .register("end_stone_pillar", properties -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> CHISILLED_END_STONE = RegistryBlocks
            .register("chiselled_end_stone", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> CHISILLED_END_STONE_UNCARVED = RegistryBlocks
            .register("chiselled_end_stone_uncarved", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> NETHERRACK_HELLSTONE_ORE = RegistryBlocks
            .register("netherrack_hellstone_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.DIAMOND)
            .oreDrop(BHItems.RAW_HELLSTONE, 1, 3)
            .register();

    public static final RegistryObject<Block> HELLSTONE_ORE = RegistryBlocks
            .register("hellstone_ore", properties -> new MagmaBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.DIAMOND)
            .oreDrop(BHItems.RAW_HELLSTONE, 1, 3)
            .register();

    public static final RegistryObject<Block> STARITE_ORE = RegistryBlocks
            .register("starite_ore", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)))
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.DIAMOND)
            .oreDrop(BHItems.RAW_STARITE, 1, 3)
            .register();

    public static final RegistryObject<Block> RAW_STARITE_BLOCK = RegistryBlocks
            .register("raw_starite_block", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)))
            .itemName("Block of Raw Starite")
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();

    public static final RegistryObject<Block> STARITE_BLOCK = RegistryBlocks
            .register("starite_block", properties -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)))
            .itemName("Block of Starite")
            .mineable(RegistryBlocks.Mineable.PICKAXE)
            .tier(RegistryBlocks.ToolTiers.IRON)
            .dropSelf()
            .register();


    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }
    public static boolean never(BlockState blockState, BlockGetter iBlockReader, BlockPos blockPos) {
        return false;
    }
    public static boolean always(BlockState blockState, BlockGetter iBlockReader, BlockPos blockPos) {
        return true;
    }
    public static void register(IEventBus eventBus) {
        RegistryEntries.BLOCKS.register(eventBus);
    }
}
