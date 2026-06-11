package com.kenhorizon.beyondhorizon.server.block.redstone_lane;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.block.BHBlockProperties;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Credit from author
// this was made of foundation of redstone lane block that tweaks,fix and modified
// reason: finding a way to create a wiring system, but this only reference that are satifised to used
// I created one but its buggy and need to optimize, for time being ill use this for now.
// https://github.com/hexnowloading/DungeonNowLoading/blob/2.11-1.20.1/common/src/main/java/dev/hexnowloading/dungeonnowloading/block/RedstoneLaneBlock.java

// TODO: Make directional path allow redstone travel above and down, and build into single block that change side (Pipe block)
// TODO: Optmized and clean some codes + Make a better textures (Copper might be best to do it)
public class RedstoneLaneBlock extends DirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<RedstoneLaneMode> REDSTONE_LANE_MODE = BHBlockProperties.REDSTONE_LANE_MODE;
    public static final IntegerProperty REDSTONE_LANE_POWER = BHBlockProperties.REDSTONE_LANE_POWER;

    public RedstoneLaneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).setValue(REDSTONE_LANE_POWER, 0));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).setValue(REDSTONE_LANE_POWER, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
        stateBuilder.add(REDSTONE_LANE_MODE);
        stateBuilder.add(REDSTONE_LANE_POWER);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldBlockState, boolean b) {
        updatePowerStrength(blockState, level, blockPos);
        super.onPlace(blockState, level, blockPos, oldBlockState, b);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (blockState.getValue(BHBlockProperties.REDSTONE_LANE_MODE) == RedstoneLaneMode.UNPOWERED) return 0;
        if (direction == Direction.DOWN) return 150;
        List<Direction> directions = getConnectionDirection(blockState);
        for (Direction laneDirection : directions) {
            if (blockGetter.getBlockState(blockPos.relative(laneDirection)).getBlock() instanceof RedstoneLaneBlock) continue;
            if (direction == laneDirection.getOpposite()) {
                return 150;
            }
        }

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
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighbourBlock, boolean b) {
        if (blockState.getValue(REDSTONE_LANE_MODE) == RedstoneLaneMode.UNPOWERED) {
            if (level.getBlockState(neighbourBlock).is(BHBlocks.REDSTONE_LANE_TRANSMITTER.get())
                    && level.getBlockState(neighbourBlock).getValue(BlockStateProperties.LIT)) {
                neighbourChangedRedstoneBlock(blockState, level, blockPos, block, neighbourBlock);
            } else if (level.getBlockState(neighbourBlock).is(Blocks.REDSTONE_BLOCK)) {
                neighbourChangedRedstoneTransmitterBlock(blockState, level, blockPos, block, neighbourBlock);
            } else if ((level.getBlockState(neighbourBlock).is(BHBlocks.REDSTONE_LANE_I.get())
                    || level.getBlockState(neighbourBlock).is(BHBlocks.REDSTONE_LANE_L.get())
                    || level.getBlockState(neighbourBlock).is(BHBlocks.REDSTONE_LANE_T.get()))
                    && (level.getBlockState(neighbourBlock).getValue(REDSTONE_LANE_MODE) == RedstoneLaneMode.POWERED)) {
                neighbourChangedLane(blockState, level, blockPos, block, neighbourBlock);
            }
        } else {
            updatePowerStrength(blockState, level, blockPos);
        }

    }

    private void updatePowerStrength(BlockState blockState, Level level, BlockPos blockPos) {
        List<BlockPos> neighborLaneBlockPosList = this.getConnectionBlockPos(blockPos, blockState);
        int power;
        int originalPower = blockState.getValue(BHBlockProperties.REDSTONE_LANE_POWER);
        boolean hasTransmitterBlock = !neighborLaneBlockPosList.stream().filter(b -> level.getBlockState(b).is(BHBlocks.REDSTONE_LANE_TRANSMITTER.get()) && level.getBlockState(b).getValue(BlockStateProperties.LIT)).toList().isEmpty();
        boolean hasRedstoneBlock = !neighborLaneBlockPosList.stream().filter(b -> level.getBlockState(b).is(Blocks.REDSTONE_BLOCK)).toList().isEmpty();
        BeyondHorizon.LOGGER.debug("is redstone block active ? {} | {}", hasRedstoneBlock, level.getBlockState(blockPos.above()));
        if (hasTransmitterBlock || (level.getBlockState(blockPos.above()).is(BHBlocks.REDSTONE_LANE_TRANSMITTER.get()) && level.getBlockState(blockPos.above()).getValue(BlockStateProperties.LIT))) {
            power = 150;
        } else if (hasRedstoneBlock || (level.getBlockState(blockPos.above()).is(Blocks.REDSTONE_BLOCK))) {
            power = 150;
        } else {
            List<BlockPos> redstoneLanePosList = neighborLaneBlockPosList.stream()
                    .filter(b -> level.getBlockState(b).getBlock() instanceof RedstoneLaneBlock)
                    .filter(b -> isLaneConnected(level, blockState, blockPos, b))
                    .toList();

            int highestPower = redstoneLanePosList.stream().mapToInt(b -> level.getBlockState(b).getValue(BHBlockProperties.REDSTONE_LANE_POWER)).max().orElse(0);
            power = Math.max(highestPower - 1, 0);

        }
        if (originalPower == power) {
//            level.neighborChanged(blockPos.above(), this, blockPos);
            return;
        }
        if (power == 0) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BHBlockProperties.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).setValue(BHBlockProperties.REDSTONE_LANE_POWER, 0));
        } else {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BHBlockProperties.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).setValue(BHBlockProperties.REDSTONE_LANE_POWER, power));
        }
        level.neighborChanged(blockPos.above(), this, blockPos);
        updateConnectedNegihbors(neighborLaneBlockPosList, level, blockPos);
    }
    private void neighbourChangedRedstoneTransmitterBlock(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighbourBlock) {

        List<BlockPos> neighborBlockPosList = getConnectionBlockPos(blockPos, blockState);
        neighborBlockPosList.add(blockPos.above());
        boolean isRedstoneBlock = false;
        for (BlockPos pos : neighborBlockPosList) {
            isRedstoneBlock = pos.equals(neighbourBlock);
            if (isRedstoneBlock) break;
        }
        if (!isRedstoneBlock) return;
        level.setBlockAndUpdate(blockPos, blockState.setValue(BHBlockProperties.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).setValue(BHBlockProperties.REDSTONE_LANE_POWER, 150));
        poweredParticle(level, blockState, blockPos);
        level.neighborChanged(blockPos.above(), this, blockPos);
        updateConnectedNeighborsWithExcluded(level, blockState, blockPos, neighbourBlock);
    }
    private void neighbourChangedRedstoneBlock(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighbourBlock) {
        List<BlockPos> neighborBlockPosList = this.getConnectionBlockPos(blockPos, blockState);
        neighborBlockPosList.add(blockPos.above());
        boolean isRedstoneBlock = false;
        for (BlockPos pos : neighborBlockPosList) {
            isRedstoneBlock = pos.equals(neighbourBlock);
            if (isRedstoneBlock) break;
        }
        if (!isRedstoneBlock) return;
        level.setBlockAndUpdate(blockPos, blockState.setValue(BHBlockProperties.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).setValue(BHBlockProperties.REDSTONE_LANE_POWER, 150));
        poweredParticle(level, blockState, blockPos);
        level.neighborChanged(blockPos.above(), this, blockPos);
        updateConnectedNeighborsWithExcluded(level, blockState, blockPos, neighbourBlock);
    }

    private void neighbourChangedLane(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighbourBlock) {
        if (!isLaneConnected(level, blockState, blockPos, neighbourBlock)) return;
        int power = level.getBlockState(neighbourBlock).getValue(BHBlockProperties.REDSTONE_LANE_POWER) - 1;
        if (power == 0) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BHBlockProperties.REDSTONE_LANE_MODE, RedstoneLaneMode.UNPOWERED).setValue(BHBlockProperties.REDSTONE_LANE_POWER, 0));
        } else {
            level.setBlockAndUpdate(blockPos, blockState.setValue(BHBlockProperties.REDSTONE_LANE_MODE, RedstoneLaneMode.POWERED).setValue(BHBlockProperties.REDSTONE_LANE_POWER, power));
            poweredParticle(level, blockState, blockPos);
        }
        level.neighborChanged(blockPos.above(), this, blockPos);
        updateConnectedNeighborsWithExcluded(level, blockState, blockPos, neighbourBlock);
    }

    private void updateConnectedNegihbors(List<BlockPos> blockPosList, Level level, BlockPos blockPos) {
        for (BlockPos pos : blockPosList) {
            level.neighborChanged(pos, this, blockPos);
        }
    }

    private void updateConnectedNeighborsWithExcluded(Level level, BlockState blockState, BlockPos blockPos, BlockPos excludedBlockPos) {
        List<BlockPos> updateTargets = getConnectionBlockPos(blockPos, blockState);
        for (BlockPos pos : updateTargets) {
            if (pos.equals(excludedBlockPos)) continue;
            level.neighborChanged(pos, this, blockPos);
        }
    }

    private boolean isLaneConnected(Level level, BlockState blockState, BlockPos originalBlockPos, BlockPos neighborBlockPos) {
        List<BlockPos> originalBlockPosList = getConnectionBlockPos(originalBlockPos, blockState);
        boolean isRedstoneBlock = false;
        for (BlockPos pos : originalBlockPosList) {
            isRedstoneBlock = pos.equals(neighborBlockPos);
            if (isRedstoneBlock) break;
        }
        if (!isRedstoneBlock) return false;
        BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
        List<BlockPos> neighborBlockPosList = getConnectionBlockPos(neighborBlockPos, neighborBlockState);
        for (BlockPos pos : neighborBlockPosList) {
            isRedstoneBlock = pos.equals(originalBlockPos);
            if (isRedstoneBlock) break;
        }
        return isRedstoneBlock;
    }

    private List<BlockPos> getConnectionBlockPos(BlockPos blockPos, BlockState blockState) {
        List<BlockPos> neighborBlockPosList = new ArrayList<>();
        Direction direction = blockState.getValue(FACING);

        if (blockState.is(BHBlocks.REDSTONE_LANE_I.get())) {
            neighborBlockPosList.add(blockPos.relative(direction));
            neighborBlockPosList.add(blockPos.relative(direction.getOpposite()));
        }

        if (blockState.is(BHBlocks.REDSTONE_LANE_L.get())) {
            neighborBlockPosList.add(blockPos.relative(direction.getCounterClockWise()));
            neighborBlockPosList.add(blockPos.relative(direction));
        }

        if (blockState.is(BHBlocks.REDSTONE_LANE_T.get())) {
            neighborBlockPosList.add(blockPos.relative(direction.getCounterClockWise()));
            neighborBlockPosList.add(blockPos.relative(direction.getClockWise()));
            neighborBlockPosList.add(blockPos.relative(direction));
        }

        return neighborBlockPosList;
    }

    private List<Direction> getConnectionDirection(BlockState blockState) {
        List<Direction> directions = new ArrayList<>();
        Direction direction = blockState.getValue(FACING);

        if (blockState.is(BHBlocks.REDSTONE_LANE_I.get())) {
            directions.add(direction);
            directions.add(direction.getOpposite());
        }

        if (blockState.is(BHBlocks.REDSTONE_LANE_L.get())) {
            directions.add(direction.getCounterClockWise());
            directions.add(direction);
        }

        if (blockState.is(BHBlocks.REDSTONE_LANE_T.get())) {
            directions.add(direction.getCounterClockWise());
            directions.add(direction.getClockWise());
            directions.add(direction);
        }

        return directions;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        switch (blockState.getValue(BHBlockProperties.REDSTONE_LANE_MODE)) {
            default:
            case UNPOWERED:
                break;
            case POWERED:
                if (randomSource.nextFloat() < 0.1F) {
                    float r = randomSource.nextFloat() - randomSource.nextFloat();
                    double x = (double)blockPos.getX() + randomSource.nextFloat();
                    double y = (double)blockPos.getY() + 1.1 + 0.1 * r;
                    double z = (double)blockPos.getZ() + randomSource.nextFloat();
                    level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
                }
                break;
        }
    }

    private void poweredParticle(Level level, BlockState blockState, BlockPos blockPos) {
        double x = (double)blockPos.getX() + 0.5;
        double y = (double)blockPos.getY() + 1.1;
        double z = (double)blockPos.getZ() + 0.5;
        ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, x, y, z, 5, 0.25D, 0.1D, 0.25D, 0.0);
    }
}
