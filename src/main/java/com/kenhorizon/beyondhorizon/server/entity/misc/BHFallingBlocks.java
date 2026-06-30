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
import org.joml.Quaternionf;

public class BHFallingBlocks extends Entity {
    public enum FallingMoveType {
        RENDER_MOVE,
        OVERALL_MOVE,
        SIMULATE_RUPTURE
    }
    public int duration;
    private static final EntityDataAccessor<String> MODE = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<BlockPos> BLOCKPOS = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Quaternionf> QUATERNION = SynchedEntityData.defineId(BHFallingBlocks.class, EntityDataSerializers.QUATERNION);

    public BHFallingBlocks(EntityType<BHFallingBlocks> type, Level level) {
        super(type, level);
        this.duration = 20;
    }

    public BHFallingBlocks(Level level, double x, double y, double z, BlockState blockState, int duration) {
        this(BHEntity.FALLING_BLOCKS.get(), level);
        this.setBlockState(blockState);
        this.setPos(x, y + (double)((1.0F - this.getBbHeight()) / 2.0F), z);
        this.setDeltaMovement(Vec3.ZERO);
        this.duration = duration;
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
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
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

        if (this.onGround() && this.tickCount > duration) {
            discard();
        }
        if (this.tickCount > 300) {
            discard();
        }

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

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tag.getCompound("blockstate")));
        this.duration = tag.getInt("duration");

    }

    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}