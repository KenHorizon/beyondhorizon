package com.kenhorizon.beyondhorizon.server.block.redstone_lane;

import com.kenhorizon.beyondhorizon.server.block.AdvancePipeBlock;
import com.kenhorizon.beyondhorizon.server.block.BHBlockProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class RedstoneWiredBlock extends Block {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((key) -> {
        return key.getKey().getAxis().isHorizontal();
    }).collect(Util.toMap());
    public RedstoneWiredBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
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
        return blockState.is(this) && blockState.is(this) == this.defaultBlockState().is(this);
    }
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return 0;

    }
    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getSignal(blockGetter, blockPos, direction);
    }
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState facingState,
                                  LevelAccessor levelAccessor, BlockPos currentPos, BlockPos facingPos) {
//        if (blockState.getValue(WATERLOGGED)) {
//            levelAccessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
//        }

        return direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), this.connectsTo(facingState, facingState.isFaceSturdy(levelAccessor, facingPos, direction.getOpposite()))) : super.updateShape(blockState, direction, facingState, levelAccessor, currentPos, facingPos);
    }
    //
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
