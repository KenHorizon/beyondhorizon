package com.kenhorizon.beyondhorizon.server.block.entity;

import com.kenhorizon.beyondhorizon.server.block.GateBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHBlockEntity;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class GateBlockBlockEntity extends BHBlockEntityBase<GateBlockBlockEntity> {
    private int animationTick = 0;
    private boolean isOpened;
    private final AnimationState openingAnimation = new AnimationState();
    private final AnimationState open = new AnimationState();
    private final AnimationState closingAnimation = new AnimationState();
    private final AnimationState close = new AnimationState();

    public GateBlockBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BHBlockEntity.GATE.get(), blockPos, blockState);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.openingAnimation.start(this.tickCount);
            return true;
        } else if (id == 2) {
            this.closingAnimation.start(this.tickCount);
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, GateBlockBlockEntity entity) {
        this.tickCount++;
        if (blockState.getBlock() instanceof GateBlocks) {
            if (blockState.getValue(GateBlocks.LIT)) {
                ++entity.animationTick;
                if (!blockState.getValue(GateBlocks.OPEN)) {
                    if (entity.animationTick == 1) {
                        this.level.blockEvent(blockPos, this.getBlockState().getBlock(), 1, 0);
                        this.particle(level, blockPos, 20);
                        level.playSound((Player)null, blockPos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 4F, level.random.nextFloat() * 0.2F + 1.0F);
                    }
                    if (entity.animationTick >= 20) {
                        if (!level.isClientSide) {
                            level.setBlock(blockPos, blockState.setValue(GateBlocks.OPEN, Boolean.TRUE), 2);
                            for (int i = 0; i <= 5; i++) {
                                BlockPos blockpos2 = blockPos.above(i);
                                BlockState blockstate = level.getBlockState(blockpos2);
                                if (blockstate.is(BHBlocks.GATE_PARTS.get())) {
                                    level.setBlock(blockpos2, blockstate.setValue(GateBlocks.GateParts.OPEN, Boolean.TRUE), 2);
                                }
                            }
                        }
                    }
                } else {
                    entity.animationTick = 0;
                    if (level.isClientSide) {
                        entity.openingAnimation.stop();
                        entity.open.startIfStopped(entity.tickCount);
                    }
                }
            }
            if (!blockState.getValue(GateBlocks.LIT)) {
                ++entity.animationTick;
                if (blockState.getValue(GateBlocks.OPEN)) {
                    if (entity.animationTick == 1) {
                        this.resetAnimationState(entity);
                        this.level.blockEvent(blockPos, this.getBlockState().getBlock(), 2, 0);
                        level.playSound((Player) null, blockPos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 4F, level.random.nextFloat() * 0.2F + 1.0F);
                    }
                    if (entity.animationTick >= 20) {
                        if (!level.isClientSide) {
                            level.setBlock(blockPos, blockState.setValue(GateBlocks.OPEN, Boolean.FALSE), 2);
                            for (int i = 0; i <= 5; i++) {
                                BlockPos blockpos2 = blockPos.above(i);
                                BlockState blockstate = level.getBlockState(blockpos2);
                                if (blockstate.is(BHBlocks.GATE_PARTS.get())) {
                                    level.setBlock(blockpos2, blockstate.setValue(GateBlocks.GateParts.OPEN, Boolean.FALSE), 2);
                                }
                            }
                        }
                    }
                } else {
                    entity.animationTick = 0;
                    if (level.isClientSide) {
                        entity.openingAnimation.stop();
                        entity.open.stop();
                        entity.closingAnimation.stop();
                        entity.close.startIfStopped(entity.tickCount);
                    }
                }
            }
        }
    }

    private void particle(Level level, BlockPos blockPos, int amount) {
        for (int i = 0; i < amount; i++) {
            level.addParticle(ParticleTypes.CLOUD, blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.SMOKE, blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
        }
    }

    private void resetAnimationState(GateBlockBlockEntity entity) {
        entity.openingAnimation.stop();
        entity.open.stop();
    }

    @Override
    public AABB getRenderBoundingBox() {
        AABB bounds = super.getRenderBoundingBox();
        bounds = bounds.expandTowards(0, 5, 0);
        return bounds;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.animationTick = compound.getInt("animationTicks");
        this.setOpened(compound.getBoolean("isOpened"));
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("animationTicks", this.animationTick);
        compound.putBoolean("isOpened", this.isOpened());
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public AnimationState getAnimationState(String input) {
        if (input == "opening") {
            return this.openingAnimation;
        } else if (input == "open") {
            return this.open;
        }  else if (input == "closing") {
            return this.closingAnimation;
        }  else if (input == "close") {
            return this.close;
        } else {
            return new AnimationState();
        }
    }
}
