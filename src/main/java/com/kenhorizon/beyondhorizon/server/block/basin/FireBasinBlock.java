package com.kenhorizon.beyondhorizon.server.block.basin;

import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FireBasinBlock extends Block implements SimpleWaterloggedBlock {
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0.75, 0, 0.75, 15.25, 14, 16.25)
    );
    private static final VoxelShape VIRTUAL_FENCE_POST = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public FireBasinBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.TRUE).setValue(HANGING, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE));
    }
    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (blockState.getValue(LIT) && entity instanceof LivingEntity target && !EnchantmentHelper.hasFrostWalker(target)) {
            if (target.hurt(level.damageSources().inFire(), (float) 1.0F)) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                if (entity.getRemainingFireTicks() == 0) {
                    entity.setSecondsOnFire(8);
                }
            }
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockstate = this.defaultBlockState().setValue(HANGING, direction == Direction.UP);
                if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return blockstate.setValue(WATERLOGGED, level.getFluidState(blockPos).getType() == Fluids.WATER);
                }
            }
        }
        boolean flag = level.getFluidState(blockPos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        Direction direction = getConnectedDirection(blockState).getOpposite();
        return Block.canSupportCenter(level, blockPos.relative(direction), direction.getOpposite());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getItem() instanceof ShovelItem && blockState.getValue(LIT)) {
            if (!level.isClientSide()) {
                stack.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                level.setBlockAndUpdate(blockPos, blockState.setValue(LIT, false));
                level.playSound((Player) null, blockPos,SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.3F, 0.6F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        if (!stack.isEmpty() && stack.is(BHItemTags.CAN_LIT_BASIN) && !blockState.getValue(LIT)) {
            if (!level.isClientSide()) {
                stack.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                level.setBlockAndUpdate(blockPos, blockState.setValue(LIT, true));
                level.playSound((Player) null, blockPos,SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.3F, 0.6F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.use(blockState, level, blockPos, player, hand, hitResult);
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos blockPos, BlockPos neighborPos) {
        if (blockState.getValue(WATERLOGGED)) {
            level.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return getConnectedDirection(blockState).getOpposite() == direction && !blockState.canSurvive(level, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, neighborState, level, blockPos, neighborPos);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, HANGING, WATERLOGGED);
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }
    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(LIT)) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
            }
        }
        if (random.nextInt(5) == 0) {
            for(int i = 0; i < random.nextInt(1) + 1; ++i) {
                level.addParticle(ParticleTypes.LAVA, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, (random.nextFloat() / 2.0F), 5.0E-5D, (random.nextFloat() / 2.0F));
            }
        }
    }
    public void makeParticles(Level level, BlockPos blockPos, boolean flag) {
        RandomSource randomsource = level.getRandom();
        level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double)blockPos.getX() + 0.5D + randomsource.nextDouble() / 3.0D * (double)(randomsource.nextBoolean() ? 1 : -1), (double)blockPos.getY() + randomsource.nextDouble() + randomsource.nextDouble(), (double)blockPos.getZ() + 0.5D + randomsource.nextDouble() / 3.0D * (double)(randomsource.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (flag) {
            level.addParticle(ParticleTypes.SMOKE, (double)blockPos.getX() + 0.5D + randomsource.nextDouble() / 4.0D * (double)(randomsource.nextBoolean() ? 1 : -1), (double)blockPos.getY() + 0.4D, (double)blockPos.getZ() + 0.5D + randomsource.nextDouble() / 4.0D * (double)(randomsource.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }
    }

    public void dowse(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide()) {
            for (int i = 0; i < 20; ++i) {
                makeParticles((Level)level, blockPos, true);
            }
        }
    }
    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (!blockState.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            boolean flag = blockState.getValue(LIT);
            if (flag) {
                if (!level.isClientSide()) {
                    level.playSound((Player)null, blockPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                dowse(level, blockPos, blockState);
            }
            level.setBlock(blockPos, blockState.setValue(WATERLOGGED, Boolean.valueOf(true)).setValue(LIT, Boolean.valueOf(false)), 3);
            level.scheduleTick(blockPos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult result, Projectile projectile) {
        BlockPos blockpos = result.getBlockPos();
        if (!level.isClientSide() && projectile.isOnFire() && projectile.mayInteract(level, blockpos) && !blockState.getValue(LIT) && !blockState.getValue(WATERLOGGED)) {
            level.setBlock(blockpos, blockState.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
        }

    }
    public boolean isSmokeyPos(Level pLevel, BlockPos pPos) {
        for(int i = 1; i <= 5; ++i) {
            BlockPos blockpos = pPos.below(i);
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (isBasinLit(blockstate)) {
                return true;
            }

            boolean flag = Shapes.joinIsNotEmpty(VIRTUAL_FENCE_POST, blockstate.getCollisionShape(pLevel, blockpos, CollisionContext.empty()), BooleanOp.AND); // FORGE: Fix MC-201374
            if (flag) {
                BlockState blockstate1 = pLevel.getBlockState(blockpos.below());
                return isBasinLit(blockstate1);
            }
        }

        return false;
    }
    public boolean isBasinLit(BlockState blockState) {
        return blockState.hasProperty(LIT) && blockState.getValue(LIT);
    }
    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos blockPos, PathComputationType type) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
    public static boolean canLight(BlockState blockState) {
        return blockState.is(BlockTags.CAMPFIRES, (base) -> {
            return base.hasProperty(WATERLOGGED) && base.hasProperty(LIT);
        }) && !blockState.getValue(WATERLOGGED) && !blockState.getValue(LIT);
    }

    protected static Direction getConnectedDirection(BlockState pState) {
        return pState.getValue(HANGING) ? Direction.DOWN : Direction.UP;
    }
}