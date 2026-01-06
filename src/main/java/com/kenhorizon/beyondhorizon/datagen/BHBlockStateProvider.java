package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.libs.client.data.BlockStateBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BHBlockStateProvider extends BlockStateBuilder {
    public BHBlockStateProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, existingFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
        this.blockWithItem(BHBlocks.VOIDSTONE);
        this.blockWithItem(BHBlocks.ADAMANTITE_ORE);
        this.blockWithItem(BHBlocks.COBALT_ORE);
        this.blockWithItem(BHBlocks.DEEPSLATE_COBALT_ORE);
        this.blockWithItem(BHBlocks.RUBY_ORE);
        this.blockWithItem(BHBlocks.DEEPSLATE_RUBY_ORE);
        this.blockWithItem(BHBlocks.SILVER_ORE);
        this.blockWithItem(BHBlocks.DEEPSLATE_SILVER_ORE);
        this.blockWithItem(BHBlocks.TITANIUM_ORE);
        this.blockWithItem(BHBlocks.HELLSTONE_ORE);
        this.blockWithItem(BHBlocks.NETHERRACK_HELLSTONE_ORE);
        this.blockWithItem(BHBlocks.HALITE_ORE);
        this.blockWithItem(BHBlocks.SULFUR_ORE);
        this.blockWithItem(BHBlocks.PALLADIUM_ORE);
        this.fullFaceBlock(BHBlocks.WORKBENCH);
        this.blockWithItem(BHBlocks.DUNGEON_BRICKS);
        this.blockWithItem(BHBlocks.SILTSTONE);
        this.slabBlocks(BHBlocks.SILTSTONE_SLAB, BHBlocks.SILTSTONE);
        this.stairsBlocks(BHBlocks.SILTSTONE_STAIRS, BHBlocks.SILTSTONE);
        this.blockWithItem(BHBlocks.SILTSTONE_BRICKS);
        this.slabBlocks(BHBlocks.SILTSTONE_BRICK_SLAB, BHBlocks.SILTSTONE_BRICKS);
        this.stairsBlocks(BHBlocks.SILTSTONE_BRICK_STAIRS, BHBlocks.SILTSTONE_BRICKS);
        this.blockWithItem(BHBlocks.RADIANCE_CRYSTRAL);
        this.blockWithItem(BHBlocks.END_STONE_TILES);
        this.axisBlock(BHBlocks.END_STONE_PILLAR);
        this.axisBlock(BHBlocks.END_GREY_PILLAR);
        this.blockWithItem(BHBlocks.CHISILLED_END_STONE);
        this.blockWithItem(BHBlocks.CHISILLED_END_STONE_UNCARVED);
        this.blockWithItem(BHBlocks.END_GREY_STONE);
        this.blockWithItem(BHBlocks.END_GREY_BRICKS);
        this.slabBlocks(BHBlocks.END_GREY_BRICK_SLAB, BHBlocks.END_GREY_BRICKS);
        this.stairsBlocks(BHBlocks.END_GREY_BRICK_STAIRS, BHBlocks.END_GREY_BRICKS);
        this.blockWithItem(BHBlocks.CUT_NETHER_BRICKS);
        this.blockWithItem(BHBlocks.CHISELLED_BLACK_NETHER_BRICKS);
        this.axisBlock(BHBlocks.NETHER_PILLAR);
        this.blockWithItem(BHBlocks.POLISHED_NETHER);
        this.slabBlocks(BHBlocks.POLISHED_NETHER_SLAB, BHBlocks.POLISHED_NETHER);
        this.stairsBlocks(BHBlocks.POLISHED_NETHER_STAIR, BHBlocks.POLISHED_NETHER);
        this.blockWithItem(BHBlocks.CUT_BLACK_NETHER_BRICKS);
        this.blockWithItem(BHBlocks.BLACK_NETHER_BRICKS);
        this.axisBlock(BHBlocks.BLACK_NETHER_PILLAR);
        this.slabBlocks(BHBlocks.BLACK_NETHER_BRICK_SLAB, BHBlocks.BLACK_NETHER_BRICKS);
        this.stairsBlocks(BHBlocks.BLACK_NETHER_BRICK_STAIR, BHBlocks.BLACK_NETHER_BRICKS);
        this.blockWithItem(BHBlocks.POLISHED_BLACK_NETHER);
        this.blockWithItem(BHBlocks.NETHER_BRICK_LANTERN);
        this.standBasinBlocks(BHBlocks.FIRE_BASIN);
        this.standWallBasinBlocks(BHBlocks.WALL_FIRE_BASIN);

    }
}
