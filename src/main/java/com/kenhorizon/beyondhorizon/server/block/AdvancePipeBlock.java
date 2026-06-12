package com.kenhorizon.beyondhorizon.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class AdvancePipeBlock extends Block {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public AdvancePipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos n = clickedPos.north();
        BlockPos s = clickedPos.south();
        BlockPos e = clickedPos.east();
        BlockPos w = clickedPos.west();
        BlockPos u = clickedPos.above();
        BlockPos d = clickedPos.below();
        BlockState nState = blockgetter.getBlockState(n);
        BlockState sState = blockgetter.getBlockState(s);
        BlockState eState = blockgetter.getBlockState(e);
        BlockState wState = blockgetter.getBlockState(w);
        BlockState uState = blockgetter.getBlockState(u);
        BlockState dState = blockgetter.getBlockState(d);
        BlockState newState = this.defaultBlockState()
                .setValue(NORTH, nState.is(this))
                .setValue(SOUTH, sState.is(this))
                .setValue(EAST, eState.is(this))
                .setValue(WEST, wState.is(this))
                .setValue(UP, uState.is(this))
                .setValue(DOWN, dState.is(this));
        return newState;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos blockpos, BlockPos neighborBlockPos) {
        BlockPos n = blockpos.north();
        BlockPos e = blockpos.east();
        BlockPos s = blockpos.south();
        BlockPos w = blockpos.west();
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();
        BlockState nState = level.getBlockState(n);
        BlockState eState = level.getBlockState(e);
        BlockState sState = level.getBlockState(s);
        BlockState wtState = level.getBlockState(w);
        BlockState uState = level.getBlockState(u);
        BlockState dState = level.getBlockState(d);
        return blockState.setValue(NORTH, nState.is(this))
                .setValue(NORTH, nState.is(this))
                .setValue(EAST, eState.is(this))
                .setValue(SOUTH, sState.is(this))
                .setValue(WEST, wtState.is(this))
                .setValue(UP, uState.is(this))
                .setValue(DOWN, dState.is(this));
    }
}
