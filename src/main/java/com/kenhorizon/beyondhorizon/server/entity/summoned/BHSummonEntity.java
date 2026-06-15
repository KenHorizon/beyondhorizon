package com.kenhorizon.beyondhorizon.server.entity.summoned;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAbilityEffectPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class BHSummonEntity extends BHLibEntity implements ILinkedEntity, TraceableEntity, OwnableEntity {
    private static final EntityDataAccessor<Integer> MAX_LIFE_SPAN = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_SPAN = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.INT);

    private LivingEntity cachedCaster;
    public static final String NBT_OWNER = "owner_uuid";
    public static final String NBT_LIFE_TICK = "life_tick";
    public static final String NBT_MAX_LIFE_TICK = "max_life_tick";
    private int lifeTick = 0;
    private int maxLifeTick = 0;

    public BHSummonEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        this.lookControl = new LookControl(this);
        this.noPhysics = true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        UUID uuid;
        if (nbt.hasUUID(NBT_OWNER)) {
            uuid = nbt.getUUID(NBT_OWNER);
        } else {
            String string = nbt.getString(NBT_OWNER);
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string);
        }
        if (uuid != null) {
            this.setOwnerUUID(uuid);
        }
        this.setLifeTick(nbt.getInt(NBT_LIFE_TICK));
        this.setMaxLifeTick(nbt.getInt(NBT_MAX_LIFE_TICK));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.getOwnerUUID() != null) {
            nbt.putUUID(NBT_OWNER, this.getOwnerUUID());
        }
        nbt.putInt(NBT_LIFE_TICK, this.getLifeTick());
        nbt.putInt(NBT_MAX_LIFE_TICK, this.getMaxLifeTick());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(ENTITY_ID, -1);
        this.entityData.define(LIFE_SPAN, 0);
        this.entityData.define(MAX_LIFE_SPAN, -1);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity == null) {
            return false;
        } else if (entity == this) {
            return true;
        } else if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity == this.getUsingEntity()) {
            return true;
        } else {
            return false;
        }
    }

    public void setEntityId(int id) {
        this.entityData.set(ENTITY_ID, id);
    }

    public int getEntityId() {
        return this.entityData.get(ENTITY_ID);
    }

    public int getLifeTick() {
        return this.level().isClientSide() ? this.entityData.get(LIFE_SPAN) : this.lifeTick;
    }

    public void setLifeTick(int lifeTick) {
        this.entityData.set(LIFE_SPAN, lifeTick);
        this.lifeTick = lifeTick;
    }

    public int getMaxLifeTick() {
        return this.level().isClientSide() ? this.entityData.get(MAX_LIFE_SPAN) : this.maxLifeTick;
    }

    public void setMaxLifeTick(int lifeSpan) {
        this.entityData.set(MAX_LIFE_SPAN, lifeSpan);
        this.maxLifeTick = lifeSpan;
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).isPresent() ? this.entityData.get(OWNER_UUID).get() : null;
    }

    public LivingEntity getUsingEntity() {
        if (this.getOwnerUUID() != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.getOwnerUUID());
            if (entity instanceof LivingEntity) {
                this.cachedCaster = (LivingEntity) entity;
                NetworkHandler.sendAll(new ServerboundAbilityEffectPacket(this, this.cachedCaster), this);
            }
        }
        return this.cachedCaster;
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.cachedCaster = (LivingEntity) entity;
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!this.haveInfiniteLifeSpan()) {
            if (this.getLifeTick() == 0) {
                this.onStartTick();
            }
            this.setLifeTick(this.getLifeTick() + 1);
            if (this.getLifeTick() >= this.getMaxLifeTick()) {
                this.discardWithParticle();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

    }

//    @Override
//    public void remove(RemovalReason reason) {
//        UUID owner = this.getOwnerUUID();
//        int overworkedLevel = this.getOverworkedLevel();
//        super.remove(reason);
//
//        if (!this.level().isClientSide() && owner != null && overworkedLevel > 0) {
//            OverworkedPenaltyUtil.refreshOwnerPenaltyIfPossible(this.level(), owner);
//        }
//    }

    protected void onStartTick() {

    }

    private void discardWithParticle() {
        if (!this.level().isClientSide) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 20, 0.3D, 0.3D, 0.3D, 0.0D);
        }
        this.discard();
    }

    public boolean haveInfiniteLifeSpan() {
        return this.getMaxLifeTick() == -1;
    }

    @Override
    public LivingEntity getOwner() {
        return this.cachedCaster;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        if (this.isCantDespawn()) {
            return false;
        } else {
            return this.getUsingEntity() != null;
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void move(MoverType type, Vec3 vec3) {
        super.move(type, vec3);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
