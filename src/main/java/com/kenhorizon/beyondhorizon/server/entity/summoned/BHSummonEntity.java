package com.kenhorizon.beyondhorizon.server.entity.summoned;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.entity.ai.SummonedFollowOwnerGoal;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAbilityEffectPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class BHSummonEntity extends BHLibEntity implements ILinkedEntity, TraceableEntity {
    private static final EntityDataAccessor<Integer> MAX_LIFE_SPAN = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_SPAN = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<UUID>> ENTITY_UUID = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedEntityData.defineId(BHSummonEntity.class, EntityDataSerializers.INT);

    private LivingEntity cachedCaster;
    public static final String NBT_OWNER = "Owner";
    public static final String NBT_LIFESPAN = "LifeSpan";
    public static final String NBT_MAX_LIFESPAN = "MaxLifeSpan";
    private int lifeSpan = 0;
    private int maxLifeSpan = 0;
    public BHSummonEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new SummonedFollowOwnerGoal(this, 2.0D, 3.0F, 2.0F, false));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENTITY_UUID, Optional.empty());
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

    public int getLifeSpan() {
        return this.level().isClientSide() ? this.entityData.get(LIFE_SPAN) : this.lifeSpan;
    }

    public void setLifeSpan(int lifeSpan) {
        this.entityData.set(LIFE_SPAN, lifeSpan);
        this.lifeSpan = lifeSpan;
    }

    public int getMaxLifeSpan() {
        return this.level().isClientSide() ? this.entityData.get(MAX_LIFE_SPAN) : this.maxLifeSpan;
    }

    public void setMaxLifeSpan(int lifeSpan) {
        this.entityData.set(MAX_LIFE_SPAN, lifeSpan);
        this.maxLifeSpan = lifeSpan;
    }

    public void setEntityUUID(UUID uuid) {
        this.entityData.set(ENTITY_UUID, Optional.ofNullable(uuid));
    }

    public Optional<UUID> getEntityUUID() {
        return this.entityData.get(ENTITY_UUID);
    }

    public LivingEntity getUsingEntity() {
        if (this.getEntityUUID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.getEntityUUID().get());
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
    public void tick() {
        super.tick();
        if (!this.haveInfiniteLifeSpan()) {
            this.setLifeSpan(this.getLifeSpan() + 1);
            if (this.getLifeSpan() >= this.getMaxLifeSpan()) {
                this.remove(RemovalReason.DISCARDED);
                this.discard();
            }
        }
    }

    public boolean haveInfiniteLifeSpan() {
        return this.getMaxLifeSpan() == -1;
    }

    @Override
    public @Nullable Entity getOwner() {
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
