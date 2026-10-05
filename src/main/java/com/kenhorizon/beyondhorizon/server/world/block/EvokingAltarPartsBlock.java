package com.kenhorizon.beyondhorizon.server.world.block;

import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class EvokingAltarPartsBlock extends BasicBlock {

    public static final BooleanProperty ACTIVE = BHBlockProperties.ACTIVE;
    private static final VoxelShape TOP_1_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 9),
            Block.box(0, 0, 0, 9, 16, 16)
    );
    private static final VoxelShape TOP_2_SHAPE = Shapes.or(
            Block.box(0, 0, 7, 16, 16, 16),
            Block.box(0, 0, 0, 9, 16, 16)
    );
    private static final VoxelShape TOP_3_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 9),
            Block.box(7, 0, 0, 16, 16, 16)
    );
    private static final VoxelShape TOP_4_SHAPE = Shapes.or(
            Block.box(0, 0, 7, 16, 16, 16),
            Block.box(7, 0, 0, 16, 16, 16)
    );

    public EvokingAltarPartsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(ACTIVE)) {
            BlockPos corner = getCornerForFurnace(level, pos, true);
            if (corner != null && corner.getY() == pos.getY() - 1) { //top
                BlockPos sub = pos.subtract(corner);
                if (sub.getX() == 0 && sub.getZ() == 0) {
                    return TOP_1_SHAPE;
                } else if (sub.getX() == 0 && sub.getZ() == 1) {
                    return TOP_2_SHAPE;
                } else if (sub.getX() == 1 && sub.getZ() == 0) {
                    return TOP_3_SHAPE;
                } else if (sub.getX() == 1 && sub.getZ() == 1) {
                    return TOP_4_SHAPE;
                }
            }
        }
        return super.getShape(state, level, pos, context);
    }
    @Nullable
    public static BlockPos getCornerForFurnace(BlockGetter levelAccessor, BlockPos componentPos, boolean postConstruction) {
        if (postConstruction) {
            for (BlockPos pos : BlockPos.betweenClosed(componentPos.getX() - 1, componentPos.getY() - 1, componentPos.getZ() - 1, componentPos.getX() + 1, componentPos.getY() + 1, componentPos.getZ() + 1)) {
                if (levelAccessor.getBlockState(pos).is(BHBlocks.EVOKING_ALTAR.get())) {
                    return pos;
                }
            }
            return null;
        } else {
            BlockPos furthest = componentPos;
            int j = 0;
            int maxDist = 1;
            while (canBecomeAComponent(levelAccessor, furthest.west(), false) && j < maxDist) {
                furthest = furthest.west();
                j++;
            }
            j = -1;
            while (canBecomeAComponent(levelAccessor, furthest.below(), false) && j < maxDist) {
                furthest = furthest.below();
                j++;
            }
            j = -1;
            while (canBecomeAComponent(levelAccessor, furthest.north(), false) && j < maxDist) {
                furthest = furthest.north();
                j++;
            }
            return canBecomeAComponent(levelAccessor, furthest, false) ? furthest : null;
        }
    }


    public static boolean isCornerForFurnace(LevelReader levelAccessor, BlockPos componentPos, boolean checkMiddle, boolean active) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = 0; z <= 1; z++) {
                    mutableBlockPos.set(componentPos.getX() + x, componentPos.getY() + y, componentPos.getZ() + z);
                    if (checkMiddle || x != 0 || y != 0 || z != 0) {
                        BlockState state = levelAccessor.getBlockState(mutableBlockPos);
                        if (!state.is(BHBlocks.EVOKING_ALTAR_PARTS.get()) || state.getValue(ACTIVE) != active) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public static boolean canBecomeAComponent(BlockGetter levelAccessor, BlockPos componentPos, boolean postConstruction) {
        BlockState state = levelAccessor.getBlockState(componentPos);
        if (postConstruction) {
            return state.is(BHBlocks.EVOKING_ALTAR_PARTS.get()) || state.is(BHBlocks.EVOKING_ALTAR.get());
        } else {
            return state.is(BHBlocks.EVOKING_ALTAR_PARTS.get()) && !state.getValue(ACTIVE);
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(ACTIVE)) {
            BlockPos corner = getCornerForFurnace(level, pos, true);
            return corner != null && level.getBlockState(corner).is(BHBlocks.EVOKING_ALTAR.get()) &&
                    isCornerForFurnace(level, corner, false, true);
        } else {
            return true;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor,
                                  BlockPos blockPos, BlockPos blockPos1) {
        if(!state.canSurvive(levelAccessor, blockPos)){
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hit) {
        return super.use(blockState, level, blockPos, player, hand, hit);
    }
}
