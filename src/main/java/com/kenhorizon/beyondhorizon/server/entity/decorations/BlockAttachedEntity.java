package com.kenhorizon.beyondhorizon.server.entity.decorations;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class BlockAttachedEntity extends Entity implements IEntityAdditionalSpawnData {
    private static final int CHECK_INTERVAL = 100;
    private int tickSinceLastCheck;
    protected BlockPos blockPos;

    public BlockAttachedEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public BlockAttachedEntity(EntityType<?> entityType, Level level, BlockPos blockPos) {
        super(entityType, level);
        this.blockPos = blockPos;
    }

    protected abstract void recalculateBoundingBox();

    @Override
    public void tick() {
        Level level = this.level();
        if (level instanceof ServerLevel sLevel) {
            this.checkBelowWorld();
            if (this.tickSinceLastCheck++ >= CHECK_INTERVAL) {
                this.tickSinceLastCheck = 0;
                this.tickAtCheckInterval();
                if (!this.isRemoved() && !this.survives()) {
                    this.discard();
                    this.dropItems(sLevel, (Entity) null);
                }
            }
        }
    }

    protected void tickAtCheckInterval() {
    }

    @Override
    public boolean ignoreExplosion() {
        return super.ignoreExplosion();
    }

    @Override
    public void move(MoverType move, Vec3 pos) {
        Level level = this.level();
        if (level instanceof ServerLevel slevel) {
            if (!this.isRemoved() && pos.length() >0.0F) {
                this.kill();
                this.dropItems(slevel, (Entity) null);
            }
        }
        super.move(move, pos);
    }

    public abstract boolean survives();

    public abstract void dropItems(ServerLevel level, @Nullable Entity causedBy);

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public void setPos(double x, double y, double z) {
        this.blockPos = BlockPos.containing(x, y, z);
        this.recalculateBoundingBox();
    }

    @Override
    protected boolean canRide(Entity entity) {
        return !(entity instanceof FakePlayer);
    }

    public BlockPos getPos() {
        return blockPos;
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {

    }

    @Override
    public void refreshDimensions() {

    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        BlockPos storedPos = new BlockPos(nbt.getInt("posX"),nbt.getInt("posY"),nbt.getInt("posZ"));
        if (storedPos != null && storedPos.closerThan(this.blockPosition(), (double) 16.0F)) {
            this.blockPos = storedPos;
        } else {
            BeyondHorizon.LOGGER.error("Block-attached entity at invalid position: {}", storedPos);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("posX", this.getPos().getX());
        nbt.putInt("posY", this.getPos().getY());
        nbt.putInt("posZ", this.getPos().getZ());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
