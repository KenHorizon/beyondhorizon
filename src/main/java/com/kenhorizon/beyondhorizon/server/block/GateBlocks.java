package com.kenhorizon.beyondhorizon.server.block;

import com.kenhorizon.beyondhorizon.server.block.entity.GateBlockBlockEntity;
import com.kenhorizon.beyondhorizon.server.init.BHBlockEntity;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GateBlocks extends BaseEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape CLOSED = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);


    public GateBlocks(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(OPEN, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, LIT);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return CLOSED;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, entity, itemStack);
        if (!level.isClientSide()) {
            for (int i = 0; i < 4; i++) {
                BlockPos abovePos = blockPos.above(i);
                BlockPos setBlockPos = abovePos;
                BlockState defaultBlockState = BHBlocks.GATE_PARTS.get().defaultBlockState();
                if (setBlockPos != blockPos) {
                    level.setBlock(setBlockPos, defaultBlockState.setValue(GateParts.Y_OFFSET ,i), 3);
                }
                level.blockUpdated(abovePos, Blocks.AIR);
                blockState.updateNeighbourShapes(level, abovePos, 3);
            }
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide() && player.isCreative()) {
            for (int i = 0; i <= 4; i++) {
                BlockPos abovePos = blockPos.above(i);
                BlockState blockstate = level.getBlockState(abovePos);
                if (blockstate.is(BHBlocks.GATE_PARTS.get())) {
                    level.setBlock(abovePos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, abovePos, Block.getId(blockstate));
                }
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GateBlockBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> tile) {
        return createTickerHelper(tile, BHBlockEntity.GATE.get(),
                ((blevel, blockPos, bblockState, blockEntity) -> {
                    blockEntity.tick(blevel, blockPos, bblockState, blockEntity);
                }));
    }

    private boolean doesFit(BlockPos pos, Level level) {
        for (int i = 0; i <= 4; i++) {
            BlockPos blockpos2 = pos.above(i);
            BlockState blockstate = level.getBlockState(blockpos2);
            if (!blockstate.canBeReplaced()) return false;
        }
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        Direction.Axis direction$axis = direction.getAxis();
        if (direction$axis == Direction.Axis.Y) {
            BlockState blockstate = this.defaultBlockState().setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
            if (blockstate.canSurvive(context.getLevel(), blockpos) && this.doesFit(blockpos, context.getLevel())) {
                return blockstate;
            }
        } else {
            BlockState blockstate1 = this.defaultBlockState().setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
            if (blockstate1.canSurvive(context.getLevel(), context.getClickedPos()) && doesFit(context.getClickedPos(), context.getLevel())) {
                return blockstate1;
            }
        }
        return null;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide()) {
            boolean flag = blockState.getValue(LIT);
            if (flag != level.hasNeighborSignal(blockPos)) {
                if (flag) {
                    level.scheduleTick(blockPos, this, 4);
                } else {
                    level.setBlock(blockPos, blockState.cycle(LIT), 2);
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(LIT) && !level.hasNeighborSignal(blockPos)) {
            level.setBlock(blockPos, blockState.cycle(LIT), 2);
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos blockPos, PathComputationType type) {
        return false;
    }

    public static class GateParts extends Block {
        public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
        public static final IntegerProperty Y_OFFSET = IntegerProperty.create("y_offset", 0, 4);

        public GateParts(Properties properties) {
            super(properties);
            this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.FALSE).setValue(Y_OFFSET, 0));

        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(OPEN, Y_OFFSET);
        }

        @Override
        public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
            return CLOSED;
        }

        @Override
        public RenderShape getRenderShape(BlockState blockState) {
            return RenderShape.INVISIBLE;
        }

        @Override
        public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos blockPos, BlockPos neighborPos) {
            BlockPos basePos = blockPos.below(blockState.getValue(Y_OFFSET));
            BlockState baseState = level.getBlockState(basePos);
            if (!baseState.is(BHBlocks.GATE.get())) {
                return Blocks.AIR.defaultBlockState();
            }
            return super.updateShape(blockState, direction, neighborState, level, blockPos, neighborPos);
        }

        @Override
        public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
            BlockPos basePos = blockPos.below(blockState.getValue(Y_OFFSET));
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(BHBlocks.GATE.get())) {
                BlockHitResult baseHitResult = new BlockHitResult(hitResult.getLocation().add(basePos.getX() - blockPos.getX(), basePos.getY() - blockPos.getY(), basePos.getZ() - blockPos.getZ()), hitResult.getDirection(), basePos, hitResult.isInside());
                return baseState.getBlock().use(baseState, level, blockPos, player, hand, baseHitResult);
            }
            return super.use(blockState, level, blockPos, player, hand, hitResult);
        }

        @Override
        public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
            BlockPos basePos = blockPos.below(blockState.getValue(Y_OFFSET));
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(BHBlocks.GATE.get())) {
                level.setBlock(basePos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, basePos, Block.getId(blockState));
            }
        }

        @Override
        public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos blockPos, PathComputationType type) {
            return false;
        }

        @Override
        public VoxelShape getVisualShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
            if (blockState.getValue(OPEN)) {
                return Shapes.empty();
            } else {
                return CLOSED;
            }
        }

        @Override
        public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
            if (blockState.getValue(OPEN)) {
                return Shapes.empty();
            } else {
                return CLOSED;
            }
        }

        @Override
        public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
            if (blockState.getValue(OPEN)) {
                return Shapes.empty();
            } else {
                return CLOSED;
            }
        }

        @Override
        public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
            return Shapes.empty();
        }

        @Override
        public Item asItem() {
            return BHBlocks.GATE.get().asItem();
        }
    }
}