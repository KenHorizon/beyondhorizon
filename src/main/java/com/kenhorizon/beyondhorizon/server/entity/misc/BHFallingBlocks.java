package com.kenhorizon.beyondhorizon.server.entity.misc;

import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BHFallingBlocks extends Entity {
    public enum FallingMoveType {
        RENDER_MOVE,
        OVERALL_MOVE,
        SIMULATE_RUPTURE
    }
    public int duration;
    public static float DROP_FACTORS = 0.1F;
    public static int MAX_ACTIVE = 600;
    public float animY = 0;
    public float prevAnimY = 0;

    private static final EntityDataAccessor<String> MODE = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> ANIM_V_Y = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<BlockPos> BLOCKPOS = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Quaternionf> QUATERNION = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.QUATERNION);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.INT);

    public BHFallingBlocks(EntityType<BHFallingBlocks> type, Level level) {
        super(type, level);
        this.setDuration(20);
    }
    public BHFallingBlocks(Level worldIn, BlockState blockState, float vy) {
        super(BHEntity.FALLING_BLOCK.get(), worldIn);
        this.setMode(FallingMoveType.RENDER_MOVE);
        this.setBlockState(blockState);
        this.setAnimVY(vy);
    }

    public BHFallingBlocks(Level level, double x, double y, double z, BlockState blockState, int duration) {
        this(BHEntity.FALLING_BLOCK.get(), level);
        this.setBlockState(blockState);
        this.setMode(FallingMoveType.OVERALL_MOVE);
        this.setPos(x, y + (double)((1.0F - this.getBbHeight()) / 2.0F), z);
        this.setDeltaMovement(Vec3.ZERO);
        this.setDuration(duration);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }
    public BHFallingBlocks(Level level, BlockState blockState, Quaternionf quaternionf, int duration, float vy) {
        super(BHEntity.FALLING_BLOCK.get(), level);
        this.setMode(FallingMoveType.SIMULATE_RUPTURE);
        this.setBlockState(blockState);
        this.setQuaternionf(quaternionf);
        this.setDuration(duration);
        this.setAnimVY(vy);
    }
    public void setStartPos(BlockPos blockPos) {
        this.entityData.set(BLOCKPOS, blockPos);
    }

    public BlockPos getStartPos() {
        return this.entityData.get(BLOCKPOS);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BLOCKPOS, BlockPos.ZERO);
        this.entityData.define(BLOCK_STATE, Blocks.AIR.defaultBlockState());
        this.entityData.define(MODE, FallingMoveType.RENDER_MOVE.toString());
        this.entityData.define(ANIM_V_Y, 1F);
        this.entityData.define(DURATION, 20);
        this.entityData.define(QUATERNION, new Quaternionf());
    }

    private void updateY(float animVY, float dropFactors) {
        prevAnimY = animY;
        animY += animVY;
        if (animY < -0.5) discard();
        setAnimVY(animVY - dropFactors);
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.entityData.set(DURATION, duration);
    }

    public int getDuration() {
        return this.level().isClientSide() ? this.entityData.get(DURATION) : this.duration;
    }

    public BlockState getBlockState() {
        return this.entityData.get(BLOCK_STATE);
    }

    public void setBlockState(BlockState blockState) {
        this.entityData.set(BLOCK_STATE, blockState);
    }

    public FallingMoveType getMode() {
        String mode = this.entityData.get(MODE);
        if (mode.isEmpty()) {
            return FallingMoveType.RENDER_MOVE;
        }
        return FallingMoveType.valueOf(mode);
    }

    public void setMode(FallingMoveType type) {
        this.entityData.set(MODE, type.toString());
    }


    @Override
    public void tick() {
        if (this.getMode() != FallingMoveType.OVERALL_MOVE) this.setDeltaMovement(0, 0 ,0);
        super.tick();
        if (this.tickCount >= MAX_ACTIVE) this.discard();
        if (this.getMode() == FallingMoveType.OVERALL_MOVE) {
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, DROP_FACTORS / 2, 0.0D));
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
            if ((this.onGround() && this.tickCount > this.getDuration()) || this.tickCount > 300) {
                this.discard();
            }
        } else if (getMode() == FallingMoveType.RENDER_MOVE) {
            this.updateY(getAnimVY(), DROP_FACTORS);
        } else {
            float animVY = getAnimVY();
            if (animVY < 0 && this.tickCount <= this.getDuration()) {
                this.prevAnimY = this.animY;
                return;
            }
            this.updateY(animVY, 0.2f);
        }
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        if (getMode() == FallingMoveType.OVERALL_MOVE) {
            super.setDeltaMovement(x, y, z);
        }
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 deltaMovement) {
        if (getMode() == FallingMoveType.OVERALL_MOVE) {
            super.setDeltaMovement(deltaMovement);
        }
    }

    public float getAnimVY() {
        return getEntityData().get(ANIM_V_Y);
    }

    private void setAnimVY(float vy) {
        getEntityData().set(ANIM_V_Y, vy);
    }

    public Quaternionf getQuaternionf() {
        return getEntityData().get(QUATERNION);
    }

    public void setQuaternionf(Quaternionf quaternionf) {
        getEntityData().set(QUATERNION, quaternionf);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        BlockState blockState = getBlockState();
        tag.put("blockstate", NbtUtils.writeBlockState(blockState));
        tag.putInt("duration", this.duration);
        tag.putInt("tickTimer", tickCount);
        tag.putFloat("vy", this.getEntityData().get(ANIM_V_Y));

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("blockstate")));
        this.duration = tag.getInt("duration");
        this.tickCount = tag.getInt("tickTimer");
        this.setAnimVY(tag.getFloat("vy"));

    }

    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}