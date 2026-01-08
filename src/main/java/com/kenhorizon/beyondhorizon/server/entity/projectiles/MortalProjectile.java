package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.kenhorizon.beyondhorizon.client.particle.IndicatorRingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.IndicatorRingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

public class MortalProjectile extends Projectile {
    public static final String NBT_GRAVITY = "Gravity";
    public static final String NBT_START_X = "StartX";
    public static final String NBT_START_Y = "StartY";
    public static final String NBT_START_Z = "StartZ";
    public static final String NBT_TARGET_X = "TargetX";
    public static final String NBT_TARGET_Y = "TargetY";
    public static final String NBT_TARGET_Z = "TargetZ";
    public static final String NBT_DAMAGE = "Damage";
    public static final String NBT_RADIUS = "Radius";
    public static final String NBT_DURATION = "Duration";
    public static final String NBT_FLIGHT_TIME = "FligtTime";

    private Vec3[] trailPositions = new Vec3[64];
    private int trailPointer = -1;
    public Vec3 startPos;
    public Vec3 targetPos;
    public float damage;
    public float radius;
    public float gravity;
    public int duration;
    public int flightTime;
    public DamageTags damageTags = DamageTags.DEFAULT;
    public float damageTypesModiifer;

    public static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> START_X = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> START_Y = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> START_Z = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TARGET_X = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TARGET_Y = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TARGET_Z = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> FLIGHT_TIME = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(MortalProjectile.class, EntityDataSerializers.INT);


    protected MortalProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setFlightTime(40);
    }

    protected MortalProjectile(EntityType<? extends Projectile> entityType, Level level, LivingEntity shooter) {
        this(entityType, level);
        this.reapplyPosition();
        this.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
        this.setOwner(shooter);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(GRAVITY, 0.03F);
        this.entityData.define(DAMAGE, 1.0F);
        this.entityData.define(RADIUS, 0.5F);
        this.entityData.define(START_X, 0.0F);
        this.entityData.define(START_Y, 0.0F);
        this.entityData.define(START_Z, 0.0F);
        this.entityData.define(TARGET_X, 0.0F);
        this.entityData.define(TARGET_Y, 0.0F);
        this.entityData.define(TARGET_Z, 0.0F);
        this.entityData.define(FLIGHT_TIME, 100);
        this.entityData.define(DURATION, 0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat(NBT_DAMAGE, this.getBaseDamage());
        nbt.putFloat(NBT_START_X, (float) this.getStartPos().x);
        nbt.putFloat(NBT_START_Y, (float) this.getStartPos().y);
        nbt.putFloat(NBT_START_Z, (float) this.getStartPos().z);
        nbt.putFloat(NBT_TARGET_X, (float) this.getTargetPos().x);
        nbt.putFloat(NBT_TARGET_Y, (float) this.getTargetPos().y);
        nbt.putFloat(NBT_TARGET_Z, (float) this.getTargetPos().z);
        nbt.putFloat(NBT_GRAVITY, this.getGravity());
        nbt.putFloat(NBT_RADIUS, this.getRadius());
        nbt.putInt(NBT_FLIGHT_TIME, this.getFlightTime());
        nbt.putInt(NBT_DURATION, this.getDuration());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setBaseDamage(nbt.getFloat(NBT_DAMAGE));
        this.setGravity(nbt.getFloat(NBT_GRAVITY));
        this.setRadius(nbt.getFloat(NBT_RADIUS));
        this.setFlightTime(nbt.getInt(NBT_FLIGHT_TIME));
        this.setDuration(nbt.getInt(NBT_DURATION));
        this.setStartPos(nbt.getFloat(NBT_START_X), nbt.getFloat(NBT_START_Y), nbt.getFloat(NBT_START_Z));
        this.setTargetPos(nbt.getFloat(NBT_TARGET_X), nbt.getFloat(NBT_TARGET_Y), nbt.getFloat(NBT_TARGET_Z));
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
        this.entityData.set(FLIGHT_TIME, flightTime);
    }

    public int getFlightTime() {
        if (this.level().isClientSide()) {
            return this.entityData.get(FLIGHT_TIME);
        } else {
            return this.flightTime;
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.entityData.set(DURATION, duration);
    }

    public int getDuration() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DURATION);
        } else {
            return this.duration;
        }
    }
    public float getBaseDamage() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DAMAGE);
        } else {
            return this.damage;
        }
    }

    public void setBaseDamage(float baseDamage) {
        this.damage = baseDamage;
        this.entityData.set(DAMAGE, baseDamage);
    }

    public float getRadius() {
        if (this.level().isClientSide()) {
            return this.entityData.get(RADIUS);
        } else {
            return this.radius;
        }
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
        this.entityData.set(GRAVITY, gravity);
    }

    public float getGravity() {
        if (this.level().isClientSide()) {
            return this.entityData.get(GRAVITY);
        } else {
            return this.gravity;
        }
    }

    public void setStartPos(Vec3 startPos) {
        this.startPos = startPos;
        this.entityData.set(START_X, startPos.toVector3f().x);
        this.entityData.set(START_Y, startPos.toVector3f().y);
        this.entityData.set(START_Z, startPos.toVector3f().z);
    }

    public void setStartPos(double x, double y, double z) {
        this.startPos = new Vec3(x, y, z);
        this.entityData.set(START_X, (float) x);
        this.entityData.set(START_Y, (float) y);
        this.entityData.set(START_Z, (float) z);
    }

    public Vec3 getStartPos() {
        double x = this.entityData.get(START_X);
        double y = this.entityData.get(START_Y);
        double z = this.entityData.get(START_Z);
        return this.level().isClientSide() ? new Vec3(x, y, z) : this.startPos;
    }

    public void setTargetPos(Vec3 startPos) {
        this.targetPos = startPos;
        this.entityData.set(TARGET_X, startPos.toVector3f().x);
        this.entityData.set(TARGET_Y, startPos.toVector3f().y);
        this.entityData.set(TARGET_Z, startPos.toVector3f().z);
    }

    public void setTargetPos(double x, double y, double z) {
        this.targetPos = new Vec3(x, y, z);
        this.entityData.set(TARGET_X, (float) x);
        this.entityData.set(TARGET_Y, (float) y);
        this.entityData.set(TARGET_Z, (float) z);
    }

    public Vec3 getTargetPos() {
        double x = this.entityData.get(TARGET_X);
        double y = this.entityData.get(TARGET_Y);
        double z = this.entityData.get(TARGET_Z);
        return this.level().isClientSide() ? new Vec3(x, y, z) : this.targetPos;
    }


    public void setRadius(float radius) {
        this.radius = radius;
        this.entityData.set(RADIUS, radius);
    }

    @Override
    public void tick() {
        this.trail();
        this.setDuration(this.getDuration() + 1);
        Entity entity = this.getOwner();
        if (this.level().isClientSide() || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
                this.onHitMortal(hitResult);
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
            double d4 = vec3.horizontalDistance();
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            this.setYRot((float) (Mth.atan2(d5, d1) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(d6, d4) * (double) (180F / (float) Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float delta = this.getDuration() / (float) this.getFlightTime();
            delta = Mth.clamp(delta, 0.0F, 1.0F);
            Vec3 movement = this.computeArcPosition(delta);
            if (tickCount == 1) {
                this.makeWarningMarkers(this.computeArcPosition(1));
            }
            Vec3 velocity = movement.subtract(this.position());
            double v0 = this.getX() + velocity.x;
            double v1 = this.getY() + velocity.y;
            double v2 = this.getZ() + velocity.z;
            this.setDeltaMovement(velocity);
            this.setPos(v0, v1, v2);
        } else {
            this.discard();
        }
    }

    protected void onHitMortal(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) hitResult);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.getLocation(), GameEvent.Context.of(this, (BlockState)null));
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            this.onHitBlock(blockHit);
            BlockPos blockpos = blockHit.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.level().broadcastEntityEvent(this, (byte) 4);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        float delta = this.getDuration() / (float) this.getFlightTime();
        delta = Mth.clamp(delta, 0.0F, 1.0F);
        if (delta >= 1.0F) {
            this.playSound(this.getImpactSounds());
            if (!this.level().isClientSide()) {
                this.level().broadcastEntityEvent(this, (byte) 4);
            }
            this.discard();
        }
    }

    protected void onImpactDamage(LivingEntity target) {
        Entity owner = this.getOwner();
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getRadius(), this.getRadius(), this.getRadius()))) {
            if (owner != null) {
                if (entity == owner) continue;
                if (owner.isAlliedTo(entity)) continue;
            }
            if (target != null && entity == target) continue;
            if (!this.level().isClientSide()) {
                LivingEntity projectileOwner = (LivingEntity) owner;
                boolean flag = DamageHandler.damage(entity, this.setDamageSource(entity), this.getBaseDamage(), this.getDamageType(), this.damageTypesModiifer);
                if (flag) {
                    if (projectileOwner != null) {
                        this.afterGotHit(entity);
                        this.doEnchantDamageEffects(projectileOwner, entity);
                    }
                }
            }
        }
    }

    private void makeWarningMarkers(Vec3 targetPos) {
        if (this.level().isClientSide()) {
            float r = ColorUtil.getFARGB(0xFF0000)[0];
            float g = ColorUtil.getFARGB(0xFF0000)[1];
            float b = ColorUtil.getFARGB(0xFF0000)[2];
            int particleDurations = (int) (this.getFlightTime() * 2.4F);
            var attackTime = new IndicatorRingParticleOptions(0, (float) Math.PI / 2, particleDurations, r, g, b, 1.0F, (16.0F * this.getRadius()), IndicatorRingParticles.Behavior.GROW);
            var rangeIndicator = new IndicatorRingParticleOptions(0, (float) Math.PI / 2, particleDurations, r, g, b, 1.0F, (16.0F * this.getRadius()), IndicatorRingParticles.Behavior.CONSTANT);
            this.level().addParticle(attackTime, targetPos.x, targetPos.y + 0.01D, targetPos.z, 0,0,0);
            this.level().addParticle(rangeIndicator, targetPos.x, targetPos.y + 0.01D, targetPos.z, 0,0,0);
        }
    }

    private Vec3 computeArcPosition(float delta) {
        Vec3 base = this.getStartPos().lerp(this.getTargetPos(), delta);
        float height = 5 * (1 - (2 * delta - 1) * (2 * delta - 1));
        return base.add(0, height, 0);
    }

    public void setDamageTag(DamageTags damageType) {
        this.damageTags = damageType;
    }

    public DamageTags getDamageType() {
        return damageTags;
    }

    public void setDamageTagModiifer(float damageTypesModiifer) {
        this.damageTypesModiifer = damageTypesModiifer;
    }

    public float getDamageTypeModifier() {
        return damageTypesModiifer;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (!this.level().isClientSide()) {
            Entity entity = hitResult.getEntity();
            LivingEntity projectileOwner = (LivingEntity) this.getOwner();
            if (entity instanceof LivingEntity target) {
                boolean flag = DamageHandler.damage(target, this.setDamageSource(target), this.getBaseDamage(), this.getDamageType(), this.damageTypesModiifer);
                if (flag) {
                    this.onImpactDamage(target);
                    this.playSound(this.getImpactSounds());
                    if (projectileOwner != null) {
                        this.afterGotHit(target);
                        this.doEnchantDamageEffects(projectileOwner, entity);
                    }
                }
            }
        }
    }

//    @Override
//    protected void onHit(HitResult hitResult) {
//        super.onHit(hitResult);
//        if (!this.level().isClientSide()) {
//            this.discard();
//        }
//    }

    public void afterGotHit(LivingEntity entity) {

    }

    public DamageSource setDamageSource(LivingEntity entity) {
        return entity.level().damageSources().mobProjectile(this, entity);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    private void trail() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI)) ;
        Vec3 trailAt = this.position().add(0, this.getBbHeight() / 2F, 0);
        if (this.trailPointer == -1) {
            Vec3 backAt = trailAt;
            for (int i = 0; i < this.trailPositions.length; i++) {
                this.trailPositions[i] = backAt;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
    }

    public boolean hasTrail() {
        return this.trailPointer != -1;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    public SoundEvent getImpactSounds() {
        return SoundEvents.GENERIC_EXPLODE;
    }

    public ParticleOptions getExplosionParticles() {
        return ParticleTypes.EXPLOSION;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            this.level().addParticle(this.getExplosionParticles(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }
}
