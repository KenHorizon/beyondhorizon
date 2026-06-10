package com.kenhorizon.beyondhorizon.server.entity;

import com.kenhorizon.beyondhorizon.server.api.block.INodeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public abstract class NodeBlockEntity extends Entity implements IEntityAdditionalSpawnData {
    protected BlockPos blockPos;
    public NodeBlockEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        AABB aabb = this.getBoundingBox();
        Vec3 diff = new Vec3(x, y, z).subtract(aabb.getCenter());
        this.setBoundingBox(aabb.move(diff));
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            return;
        }
        boolean valid = this.level().getBlockState(this.blockPosition()).getBlock() instanceof INodeBlock;
        if (this.isVehicle() && valid) return;
        this.discard();
    }

    @Override
    protected boolean canRide(Entity entity) {
        return super.canRide(entity);
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if (entity instanceof TamableAnimal animal)
            animal.setInSittingPose(false);
    }



    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }

    public BlockPos getPos() {
        return blockPos;
    }

}
