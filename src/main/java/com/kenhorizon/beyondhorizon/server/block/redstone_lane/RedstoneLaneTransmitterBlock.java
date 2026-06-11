package com.kenhorizon.beyondhorizon.server.block.redstone_lane;

import com.kenhorizon.beyondhorizon.server.block.BHBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.ticks.TickPriority;

public class RedstoneLaneTransmitterBlock extends Block {
    public static final BooleanProperty LIT =  RedstoneTorchBlock.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public RedstoneLaneTransmitterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        if (level.isClientSide()) {
            return;
        }
        this.checkTickOnNeighbor(level, pos, state);
    }

    protected void checkTickOnNeighbor(Level level, BlockPos blockPos, BlockState blockState) {
        boolean flag = blockState.getValue(LIT);
        boolean flag1 = this.shouldTurnOn(level, blockPos, blockState);
        if (flag != flag1 && !level.getBlockTicks().willTickThisTick(blockPos, this)) {
            TickPriority tickpriority = TickPriority.HIGH;
            if (this.shouldPrioritize(level, blockPos, blockState)) {
                tickpriority = TickPriority.EXTREMELY_HIGH;
            } else if (flag) {
                tickpriority = TickPriority.VERY_HIGH;
            }

            level.scheduleTick(blockPos, this, 2, tickpriority);
        }
    }
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public static boolean isPowered(BlockState blockState) {
        return blockState.getBlock() instanceof RedstoneLaneTransmitterBlock;
    }

    public boolean shouldPrioritize(BlockGetter level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(FACING).getOpposite();
        BlockState blockstate = level.getBlockState(blockPos.relative(direction));
        return isPowered(blockstate) && blockstate.getValue(FACING) != direction;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        if (this.shouldTurnOn(level, blockPos, blockState)) {
            level.scheduleTick(blockPos, this, 1);
        }

    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
        boolean flag = blockState.getValue(LIT);
        boolean flag1 = shouldTurnOn(level, blockPos, blockState);
        if (flag && !flag1) {
            level.setBlock(blockPos, blockState.setValue(LIT, Boolean.FALSE), 2);
        } else if (!flag) {
            level.setBlock(blockPos, blockState.setValue(LIT, Boolean.TRUE), 2);
            if (!flag1) {
                level.scheduleTick(blockPos, this, 2, TickPriority.VERY_HIGH);
            }
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState pOldState, boolean isMoving) {
        this.updateNeighborsInFront(level, blockPos, blockState);
    }

    protected void updateNeighborsInFront(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(FACING);
        BlockPos relative = blockPos.relative(direction.getOpposite());
        if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, blockPos, level.getBlockState(blockPos), java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
            return;
        level.neighborChanged(relative, this, blockPos);
        level.updateNeighborsAtExceptFromFacing(relative, this, direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    protected boolean shouldTurnOn(Level level, BlockPos blockPos, BlockState blockState) {
        return this.getInputSignal(level, blockPos, blockState) > 0;
    }

    protected int getInputSignal(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(FACING);
        BlockPos blockpos = blockPos.relative(direction);
        int i = level.getSignal(blockpos, direction);
        if (i >= 150) {
            return i;
        } else {
            BlockState blockstate = level.getBlockState(blockpos);
            return Math.max(i, (blockstate.getBlock() instanceof RedstoneLaneBlock || blockstate.getBlock() instanceof RedstoneLaneTransmitterBlock) ? blockstate.getValue(BHBlockProperties.REDSTONE_LANE_POWER) : 0);
        }
    }
    @Override
    public int getSignal(BlockState blockState, BlockGetter level, BlockPos blockPos, Direction direction) {
        if (!blockState.getValue(LIT)) {
            return 0;
        } else {
            return blockState.getValue(FACING) == direction ? 150 : 0;
        }
    }
}
