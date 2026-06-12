package com.kenhorizon.beyondhorizon.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class AdvancePipeBlock extends Block {
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
        BlockPos n = clickedPos.north();
        BlockPos s = clickedPos.south();
        BlockPos e = clickedPos.east();
        BlockPos w = clickedPos.west();
        BlockState nState = blockgetter.getBlockState(n);
        BlockState SState = blockgetter.getBlockState(s);
        BlockState eState = blockgetter.getBlockState(e);
        BlockState wState = blockgetter.getBlockState(w);
        return super.getStateForPlacement(context)
                .setValue(NORTH, this.connectsTo(nState, nState.isFaceSturdy(blockgetter, n, Direction.SOUTH)))
                .setValue(SOUTH, this.connectsTo(nState, SState.isFaceSturdy(blockgetter, s, Direction.NORTH)))
                .setValue(EAST, this.connectsTo(nState, eState.isFaceSturdy(blockgetter, e, Direction.WEST)))
                .setValue(WEST, this.connectsTo(nState, wState.isFaceSturdy(blockgetter, w, Direction.EAST)));
    }

    public boolean connectsTo(BlockState blockState, boolean solid) {
        boolean flag = this.matchedBlocks(blockState);
        return !isExceptionForConnection(blockState) && solid || flag;
    }

    private boolean matchedBlocks(BlockState blockState) {
        return blockState.is(this);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90:
                return blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default:
                return blockState;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK:
                return blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default:
                return super.mirror(blockState, mirror);
        }
    }
}
