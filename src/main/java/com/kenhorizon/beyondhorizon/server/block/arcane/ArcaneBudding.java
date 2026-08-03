package com.kenhorizon.beyondhorizon.server.block.arcane;

import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ArcaneBudding extends ArcaneBlock {
    public static final int GROWTH_CHANCE = 5;
    public ArcaneBudding(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(GROWTH_CHANCE) == 0) {
            Block block = BHBlocks.ARCANE_BUDDING_FULL.get();
            BlockState newState = block.defaultBlockState();
            level.setBlockAndUpdate(pos, newState);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!player.isCreative() && state.is(BHBlocks.ARCANE_BUDDING_FULL.get())) {
            Block block = BHBlocks.ARCANE_BUDDING.get();
            BlockState newState = block.defaultBlockState();
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, state));
            level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide() ? 11 : 3);
            return false;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
