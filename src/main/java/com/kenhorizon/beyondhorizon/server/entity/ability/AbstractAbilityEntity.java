package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAbilityEffectPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import java.util.Optional;
import java.util.UUID;

public abstract class AbstractAbilityEntity extends Entity implements ILinkedEntity {
    protected float damage;
    protected boolean sentSpikeEvent;
    protected int duration = 20;
    protected int lifespan;
    protected int delay;
    private LivingEntity cachedCaster;
    private LivingEntity cachedTarget;
    private static final EntityDataAccessor<Optional<UUID>> CASTER = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFE_SPAN = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IGNORE_RESISTANCE = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IGNORE_IMMUNITY_FRAME = SynchedEntityData.defineId(AbstractAbilityEntity.class, EntityDataSerializers.BOOLEAN);
    public static final String NBT_DURATION = "Duration";
    public static final String NBT_DELAY = "Delay";
    public static final String NBT_LIFESPAN = "LifeSpan";
    public static final String NBT_RADIUS = "Radius";
    public static final String NBT_DAMGE = "Damage";
    public static final String NBT_IGNORE_KNOCKBACK = "IgnoreKnockback";
    public static final String NBT_IGNORE_IMMUNITY_FRAME = "IgnoreImmunityFrame";
    public static final String NBT_OWNER = "Owner";
    public AbstractAbilityEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CASTER, Optional.empty());
        this.entityData.define(TARGET, Optional.empty());
        this.entityData.define(DELAY, 0);
        this.entityData.define(DURATION, 0);
        this.entityData.define(LIFE_SPAN, 5);
        this.entityData.define(RADIUS, 1.0F);
        this.entityData.define(DAMAGE, 5.0F);
        this.entityData.define(IGNORE_RESISTANCE, false);
        this.entityData.define(IGNORE_IMMUNITY_FRAME, false);
    }
    public Optional<UUID> getCasterID() {
        return this.entityData.get(CASTER);
    }

    public void setCasterID(UUID id) {
        this.entityData.set(CASTER, Optional.of(id));
    }

    public Optional<UUID> getTargetID() {
        return this.entityData.get(TARGET);
    }

    public void setTargetID(UUID id) {
        this.entityData.set(TARGET, Optional.of(id));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt(NBT_DURATION, this.getDuration());
        nbt.putInt(NBT_LIFESPAN, this.getLifeTime());
        nbt.putInt(NBT_DELAY, this.getDelay());
        nbt.putFloat(NBT_RADIUS, this.getRadius());
        nbt.putFloat(NBT_DAMGE, this.getBaseDamage());
        nbt.putBoolean(NBT_IGNORE_KNOCKBACK, this.isIgnoreResistance());
        nbt.putBoolean(NBT_IGNORE_IMMUNITY_FRAME, this.isIgnoreIFrame());
        if (this.getCasterID().isPresent()) {
            nbt.putUUID(NBT_OWNER, this.getCasterID().get());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.setDuration(nbt.getInt(NBT_DURATION));
        this.setLifeTime(nbt.getInt(NBT_LIFESPAN));
        this.setDelay(nbt.getInt(NBT_DELAY));
        this.setRadius(nbt.getFloat(NBT_RADIUS));
        this.setBaseDamage(nbt.getFloat(NBT_DAMGE));
        this.setIgnoreResistance(nbt.getBoolean(NBT_IGNORE_KNOCKBACK));
        this.setIgnoreIFrame(nbt.getBoolean(NBT_IGNORE_IMMUNITY_FRAME));
        this.setCasterID(nbt.getUUID(NBT_OWNER));
    }

    public void setBaseDamage(float baseDamage) {
        this.entityData.set(DAMAGE, baseDamage);
        this.damage = baseDamage;
    }

    public float getBaseDamage() {
        return damage;
    }

    public void setRadius(float radius) {
        this.entityData.set(RADIUS, radius);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setIgnoreIFrame(boolean shallIgnoreResistance) {
        this.entityData.set(IGNORE_IMMUNITY_FRAME, shallIgnoreResistance);
    }

    public boolean isIgnoreIFrame()  {
        return this.entityData.get(IGNORE_IMMUNITY_FRAME);
    }

    public void setIgnoreResistance(boolean shallIgnoreResistance) {
        this.entityData.set(IGNORE_RESISTANCE, shallIgnoreResistance);
    }

    public boolean isIgnoreResistance()  {
        return this.entityData.get(IGNORE_RESISTANCE);
    }

    public void setDelay(int delay) {
        this.entityData.set(DELAY, delay);
        this.delay = delay;
    }

    public int getDelay() {
        return this.level().isClientSide() ? this.entityData.get(DELAY) : this.delay;
    }

    public void setDuration(int seconds) {
        this.entityData.set(DURATION, seconds);
        this.duration = seconds;
    }

    public int getDuration() {
        return this.level().isClientSide() ? this.entityData.get(DURATION) : this.duration;
    }

    public void setLifeTime(int seconds) {
        this.entityData.set(LIFE_SPAN, seconds);
        this.lifespan = seconds;
    }

    public int getLifeTime() {
        return this.level().isClientSide() ? this.entityData.get(LIFE_SPAN) : this.lifespan;
    }

    protected float damage(LivingEntity target, float damage) {
        return damage;
    }

    public LivingEntity getCaster() {
        if (this.cachedCaster != null && !this.cachedCaster.isRemoved()) {
            return this.cachedCaster;
        } else if (this.getCasterID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.getCasterID().get());
            if (entity instanceof LivingEntity) {
                this.cachedCaster = (LivingEntity) entity;
                NetworkHandler.sendAll(new ServerboundAbilityEffectPacket(this, this.cachedCaster),this);
            }
            return this.cachedCaster;
        } else {
            return null;
        }
    }

    public LivingEntity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else if (this.getTargetID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.getTargetID().get());
            if (entity instanceof LivingEntity) {
                this.cachedTarget = (LivingEntity) entity;
            }
            return this.cachedTarget;
        } else {
            return null;
        }
    }

    protected void onStart() {}

    protected void onDuration() {}

    protected void onEnd() {}

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 1 && this.getCaster() == null) this.discard();
        if (getCaster() != null && !getCaster().isAlive()) this.discard();
        if (this.getLifeTime() <= (this.getDelay())) {
            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }
            this.onStart();
        }
        this.setLifeTime(this.getLifeTime() + 1);
        this.onDuration();
        if (this.getLifeTime() >= (this.getDuration() + this.getDelay()) - 1) {
            this.onEnd();
        }
        if (this.getLifeTime() >= (this.getDuration() + this.getDelay())) {
            this.discard();
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.cachedCaster = (LivingEntity) entity;
        }
    }

    public boolean checkEntity(Entity entity) {
        Vec3 from = this.position();
        int numChecks = 3;
        for (int i = 0; i < numChecks; i++) {
            float increment = entity.getBbHeight() / (numChecks + 1);
            Vec3 to = entity.position().add(0, increment * (i + 1), 0);
            BlockHitResult result = level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (result.getType() != HitResult.Type.BLOCK) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void push(Entity entity) {}

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        LivingEntity entity = this.cachedCaster;
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Entity entity = this.level().getEntity(packet.getData());
        if (entity instanceof LivingEntity) {
            cachedCaster = (LivingEntity) entity;
        }
    }
}
