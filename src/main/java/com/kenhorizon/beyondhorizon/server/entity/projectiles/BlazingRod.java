package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class BlazingRod extends Projectile {
    public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(BlazingRod.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> CAN_LIGHT_FIRE = SynchedEntityData.defineId(BlazingRod.class, EntityDataSerializers.BOOLEAN);
    protected float baseDamage = 1.0F;
    protected boolean canLightFire;
    public static final String NBT_CAN_LIGHT_FIRE = "CanLightFire";
    public static final String NBT_DAMAGE = "Damage";

    public BlazingRod(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public BlazingRod(Level level, double x, double y, double z, LivingEntity shooter) {
        this(BHEntity.BLAZING_ROD.get(), level);
        this.setOwner(shooter);
        this.setCanLightFire(true);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DAMAGE, 1.0F);
        this.entityData.define(CAN_LIGHT_FIRE, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBaseDamage(tag.getFloat(NBT_DAMAGE));
        setCanLightFire(tag.getBoolean(NBT_CAN_LIGHT_FIRE));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat(NBT_DAMAGE, this.getBaseDamage());
        tag.putBoolean(NBT_CAN_LIGHT_FIRE, this.isCanLightFire());
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public float getBaseDamage() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DAMAGE);
        } else {
            return this.baseDamage;
        }
    }
    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
        this.entityData.set(DAMAGE, baseDamage);
    }

    public boolean isCanLightFire() {
        if (this.level().isClientSide()) {
            return this.entityData.get(CAN_LIGHT_FIRE);
        } else {
            return this.canLightFire;
        }
    }
    public void setCanLightFire(boolean value) {
        this.canLightFire = value;
        this.entityData.set(CAN_LIGHT_FIRE, value);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide()) {
            Entity entity = hitResult.getEntity();
            LivingEntity projectileOwner = (LivingEntity) this.getOwner();
            entity.setSecondsOnFire(5);
            if (entity.hurt(BHDamageTypes.blazingRod(this, projectileOwner), this.getBaseDamage())) {
                if (projectileOwner != null) {
                    this.doEnchantDamageEffects(projectileOwner, entity);
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide()) {
            Entity entity = this.getOwner();
            if (isCanLightFire()) {
                BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                if (this.level().isEmptyBlock(blockPos)) {
                    this.level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
                }
            } else {
                if (!(entity instanceof Mob) || ForgeEventFactory.getMobGriefingEvent(this.level(), entity)) {
                    BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    if (this.level().isEmptyBlock(blockPos)) {
                        this.level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
                    }
                }
            }
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level().isClientSide()) {
            this.discard();
        }
    }
    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide() || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double k0 = this.getX() + vec3.x;
            double k1 = this.getY() + vec3.y;
            double k2 = this.getZ() + vec3.z;
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, k0 - vec3.x * 0.25D, k1 - vec3.y * 0.25D, k2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
            }

            if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
                double d0 = vec3.horizontalDistance();
                this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
                this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();

            this.setYRot((float) (Mth.atan2(d5, d1) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(d6, d4) * (double) (180F / (float) Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            float f1 = 0.05F;
            float sqrt = (float)this.getDeltaMovement().length();
            if (sqrt < 0.1F) {
                this.discard();
            }
            this.setDeltaMovement(vec3.scale((double) f));

            this.setPos(d7, d2, d3);
        } else {
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && !target.noPhysics;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        return false;
    }
}
