package com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class InfernoShield extends BHLibEntity implements ILinkedEntity {
    private static final EntityDataAccessor<Float> LIFE_SPAN = SynchedEntityData.defineId(InfernoShield.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ROTATE_OFFSET = SynchedEntityData.defineId(InfernoShield.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ORBIT_SCALE = SynchedEntityData.defineId(InfernoShield.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Optional<UUID>> ENTITY_UUID = SynchedEntityData.defineId(InfernoShield.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedEntityData.defineId(InfernoShield.class, EntityDataSerializers.INT);
    private LivingEntity cachedCaster;
    public static final String NBT_OWNER = "owner";
    public static final String NBT_ORBIT_SCALE = "orbit_scale";
    public static final String NBT_LIFESPAN = "life_span";
    public static final String NBT_ROTATION_OFFSET = "rotation_offset";

    public InfernoShield(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public InfernoShield(Level level, LivingEntity caster, float orbitScale, float rotationOffset) {
        super(BHEntity.INFERNO_SHIELD.get(), level);
        this.setEntityUUID(caster.getUUID());
        this.setEntityId(caster.getId());
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
        this.setOrbitScale(orbitScale);
        this.setRotateOffset(rotationOffset);
    }

    public static AttributeSupplier createAttributes() {
        return createEntityAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .add(Attributes.ATTACK_DAMAGE, 1.0F)
                .add(Attributes.FOLLOW_RANGE, 24.0F)
                .build();
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (entity instanceof AbstractArrow) {
            return false;
        } else if (source.is(DamageTypes.FALL)) {
            return false;
        } else {
            if (entity instanceof LivingEntity target) {
                ItemStack itemStack = target.getMainHandItem();
                if (itemStack.is(ItemTags.AXES)) {
                    amount *= 2.0F;
                }
            }
            return super.hurt(source, amount);
        }
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENTITY_UUID, Optional.empty());
        this.entityData.define(ENTITY_ID, -1);
        this.entityData.define(ROTATE_OFFSET, 0.0F);
        this.entityData.define(ORBIT_SCALE, 0.0F);
        this.entityData.define(LIFE_SPAN, 40.0F);
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
        } else if (entity instanceof Blaze) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.hasUUID(NBT_OWNER)) {
            this.setEntityUUID(nbt.getUUID(NBT_OWNER));
        }
        this.setOrbitScale(nbt.getFloat(NBT_ORBIT_SCALE));
        this.setLifeSpan(nbt.getInt(NBT_LIFESPAN));
        this.setRotateOffset(nbt.getInt(NBT_ROTATION_OFFSET));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        if (getEntityUUID().isPresent()) {
            nbt.putUUID(NBT_OWNER, this.getEntityUUID().get());
        }
        nbt.putFloat(NBT_ORBIT_SCALE, this.getOrbitScale());
        nbt.putFloat(NBT_LIFESPAN, this.getLifeSpan());
        nbt.putFloat(NBT_ROTATION_OFFSET, this.getRotateOffset());
    }
    public void setEntityId(int id) {
        this.entityData.set(ENTITY_ID, id);
    }

    public int getEntityId() {
        return this.entityData.get(ENTITY_ID);
    }
    private float getOrbitScale() {
        return this.entityData.get(ORBIT_SCALE);
    }

    public void setOrbitScale(float orbitScale) {
        this.entityData.set(ORBIT_SCALE, orbitScale);
    }

    private float getRotateOffset() {
        return this.entityData.get(ROTATE_OFFSET);
    }

    public void setRotateOffset(float rotateOffset) {
        this.entityData.set(ROTATE_OFFSET, rotateOffset);
    }

    public float getLifeSpan() {
        return this.entityData.get(LIFE_SPAN);
    }

    public void setLifeSpan(float lifeSpan) {
        this.entityData.set(LIFE_SPAN, lifeSpan);
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
        this.reapplyPosition();
        if (this.tickCount % 20L == 0) {
            this.repelEntities(1.4F, 4, 1.4F, 3.5F, this.getUsingEntity());
        }
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d4 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        Entity owner = this.getUsingEntity();
        if (owner != null && !owner.isAlive()) discard();
        if (owner != null) {
            if (owner instanceof BlazingInferno blazingInferno) {
                if (blazingInferno.deathTime > 0) {
                    this.kill();
                }
            }
            Entity entity = this.getEntityId() == -1 ? null : level().getEntity(this.getEntityId());
            this.infernoShieldTicks(entity != null ? entity : owner);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return this.getUsingEntity() != null;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void move(MoverType type, Vec3 vec3) {
        super.move(type, vec3);
        this.checkInsideBlocks();
    }

//    @Override
//    public SoundEvent getDeathAnimationSound() {
//        return BHSounds.INFERNO_SHIELD_EXPLOSION.get();
//    }

    protected static float lerpRotation(float pCurrentRotation, float pTargetRotation) {
        while(pTargetRotation - pCurrentRotation < -180.0F) {
            pCurrentRotation -= 360.0F;
        }

        while(pTargetRotation - pCurrentRotation >= 180.0F) {
            pCurrentRotation += 360.0F;
        }

        return Mth.lerp(0.2F, pCurrentRotation, pTargetRotation);
    }


    private void infernoShieldTicks(Entity entity) {
        float rot = this.getRotateOffset() + entity.tickCount * 7;
        Vec3 orbitBy = new Vec3(0.0D, 0.5D, 2.0D).yRot((float) -Math.toRadians(rot));
        Vec3 orbitTarget = entity.position().add(orbitBy).subtract(this.position());
        this.setXRot(10.0F);
        this.setDeltaMovement(orbitTarget.scale(getOrbitScale()));
        this.noPhysics = true;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        this.repelEntities(1.4F, 4, 1.4F, 3.5F, this.getUsingEntity());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
