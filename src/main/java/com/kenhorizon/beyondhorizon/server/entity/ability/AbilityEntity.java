package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.util.ControlledAnimation;
import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageScaling;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.*;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbilityEntity extends Entity implements ILinkedEntity, TraceableEntity {
    protected float damage = 5.0F;
    protected float radius = 5.0F;
    protected boolean sentEventSpike = false;
    protected int duration = 60;
    protected int lifespan = 0;
    protected int delay = 0;
    protected DamageType damageType = DamageType.PHYSICAL_DAMAGE;
    protected DamageScaling damageScaling;
    private float damageTagModifiers = 0.0F;
    private LivingEntity cachedCaster;
    private LivingEntity cachedTarget;
    private ControlledAnimation animation = new ControlledAnimation(0);
    private static final EntityDataAccessor<Optional<UUID>> CASTER = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DAMAGE_TYPE = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IGNORE_RESISTANCE = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IGNORE_IMMUNITY_FRAME = SynchedEntityData.defineId(AbilityEntity.class, EntityDataSerializers.BOOLEAN);
    public static final String NBT_DURATION = "Duration";
    public static final String NBT_DELAY = "Delay";
    public static final String NBT_LIFESPAN = "LifeSpan";
    public static final String NBT_RADIUS = "Radius";
    public static final String NBT_DAMGE = "Damage";
    public static final String NBT_DAMGE_TYPE = "DamageType";
    public static final String NBT_IGNORE_KNOCKBACK = "IgnoreKnockback";
    public static final String NBT_IGNORE_IMMUNITY_FRAME = "IgnoreImmunityFrame";
    public static final String NBT_OWNER = "Owner";
    protected boolean clientSideStarted = false;

    public AbilityEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CASTER, Optional.empty());
        this.entityData.define(TARGET, Optional.empty());
        this.entityData.define(DAMAGE_TYPE, 0);
        this.entityData.define(RADIUS, 1.0F);
        this.entityData.define(DAMAGE, 5.0F);
        this.entityData.define(IGNORE_RESISTANCE, false);
        this.entityData.define(IGNORE_IMMUNITY_FRAME, false);
    }

    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
        this.entityData.set(DAMAGE_TYPE, damageType.ordinal());
    }

    public DamageType getDamageType() {
        if (this.level().isClientSide()) {
            return DamageType.values()[this.entityData.get(DAMAGE_TYPE)];
        } else {
            return damageType;
        }
    }

    public Optional<UUID> getCasterID() {
        return this.entityData.get(CASTER);
    }

    public void setCaster(LivingEntity caster) {
        this.cachedCaster = caster;
        this.setCasterID(caster.getUUID());
    }

    public void setCasterID(UUID id) {
        this.entityData.set(CASTER, Optional.of(id));
    }

    public void setTarget(LivingEntity cachedTarget) {
        this.cachedTarget = cachedTarget;
        this.entityData.set(TARGET, Optional.of(cachedTarget.getUUID()));
    }

    public Optional<UUID> getTargetID() {
        return this.entityData.get(TARGET);
    }

    public void setTargetID(UUID id) {
        this.entityData.set(TARGET, Optional.of(id));
    }

    public void setDamageTags(DamageScaling damageScaling, float damageTagModifiers) {
        this.damageScaling = damageScaling;
        this.damageTagModifiers = damageTagModifiers;
    }

    public DamageScaling getDamageTags() {
        return this.damageScaling;
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
        if (nbt.contains(NBT_OWNER)) {
            this.setCasterID(nbt.getUUID(NBT_OWNER));
        }
    }

    public void setBaseDamage(float baseDamage) {
        this.entityData.set(DAMAGE, baseDamage);
        this.damage = baseDamage;
    }

    public float getBaseDamage() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DAMAGE);
        } else {
            return damage;
        }
    }

    public void setRadius(float radius) {
        this.entityData.set(RADIUS, radius);
        this.radius = radius;
    }

    public float getRadius() {
        if (this.level().isClientSide()) {
            return this.entityData.get(RADIUS);
        } else {
            return this.radius;
        }
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
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDuration(int seconds) {
        this.duration = seconds;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setLifeTime(int seconds) {
        this.lifespan = seconds;
    }

    public int getLifeTime() {
        return this.lifespan;
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
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox().inflate(this.getRadius());
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
//            BeyondHorizon.LOGGER.debug("[Ability entity] Client Sided Started!");
            this.clientSideStarted = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.clientSideStarted) {
                this.animation.increaseTimer();
                this.setLifeTime(this.getLifeTime() + 1);
            }
            this.spawnParticles();
        }
        if (this.getDelay() <= 0) {
            this.onDuration();
            if (!this.sentEventSpike) {
                this.level().broadcastEntityEvent(this, (byte) 4);
                this.sentEventSpike = true;
            }
            if (this.getLifeTime() == (this.getDelay())) {
                this.onStart();
            }
            this.setLifeTime(this.getLifeTime() + 1);
            if (this.getLifeTime() > this.getDuration() - 1) {
                this.onEnd();
            }

            if (this.getLifeTime() >= this.getDuration()) {
                this.discard();
            }
        } else {
            if (this.getDelay() > 0) {
                this.setDelay(this.getDelay() - 1);
            }
        }
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.cachedCaster = (LivingEntity) entity;
        }
    }

    @Override
    public @Nullable Entity getOwner() {
        return this.cachedCaster;
    }

    protected boolean dealDamage(LivingEntity entity, DamageScaling damageScaling, float damageTagsModifiers, float damage) {
        return DamageHandler.damage(entity, false, this.setSourceDamage(entity), damageScaling, damageTagsModifiers, damage);
    }

    public DamageSource setSourceDamage(LivingEntity entity) {
        return BHDamageTypes.physicalDamage(this, entity);
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

    protected void checkEntityHit() {
        if (!level().isClientSide()) {
            for (Entity entity : this.getSubEntityCollisions()) {
                this.onHitEntity(new EntityHitResult(entity));
            }
        }
    }

    protected Set<Entity> getSubEntityCollisions() {
        List<Entity> collisions = new ArrayList<>(this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius())));
        return collisions.stream().filter(target ->
                target instanceof LivingEntity && target != getCaster()
        ).collect(Collectors.toSet());
    }

    protected void onHitEntity(EntityHitResult hitResult) {

    }
    protected void spawnParticles() {

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

    public List<Entity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(Entity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= getY() + dY);
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
