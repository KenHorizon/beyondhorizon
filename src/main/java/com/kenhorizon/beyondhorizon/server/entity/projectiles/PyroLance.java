package com.kenhorizon.beyondhorizon.server.entity.projectiles;


import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.server.util.RaycastUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PyroLance extends ExtendedProjectile {
    public static float MOTION = 0.99F;
    public float wantedX = 0.0F;
    public float wantedY = 0.0F;
    public float wantedZ = 0.0F;
    public static final EntityDataAccessor<Float> WANTED_X = SynchedEntityData.defineId(PyroLance.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> WANTED_Y = SynchedEntityData.defineId(PyroLance.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> WANTED_Z = SynchedEntityData.defineId(PyroLance.class, EntityDataSerializers.FLOAT);
    public static final String NBT_TARGET_ID = "TargetUUID";
    public static final String NBT_WANTED_X = "TargetWantedX";
    public static final String NBT_WANTED_Y = "TargetWantedY";
    public static final String NBT_WANTED_Z = "TargetWantedZ";

    public PyroLance(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setDuration(160);
        this.setIgniteAttack(true);
    }

    public PyroLance(Level level, LivingEntity shooter) {
        this(BHEntity.PYRO_LANCE.get(), level);
        this.setOwner(shooter);
        this.setRot(shooter.getYRot(), shooter.getXRot());
        this.moveTo(shooter.getX(), shooter.getY(), shooter.getZ(), shooter.getYRot(), shooter.getXRot());
    }
    @Override
    protected void defineSynchedData() {
        this.entityData.define(WANTED_X, 0.0F);
        this.entityData.define(WANTED_Y, 0.0F);
        this.entityData.define(WANTED_Z, 0.0F);
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat(NBT_WANTED_X, this.getWantedX());
        tag.putFloat(NBT_WANTED_Y, this.getWantedY());
        tag.putFloat(NBT_WANTED_Z, this.getWantedZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setWantedTarget(tag.getFloat(NBT_WANTED_X), tag.getFloat(NBT_WANTED_Y), tag.getFloat(NBT_WANTED_Z));
    }

    public void setWantedTarget(float x, float y, float z) {
        this.entityData.set(WANTED_X, x);
        this.entityData.set(WANTED_Y, y);
        this.entityData.set(WANTED_Z, z);
        this.wantedX = x;
        this.wantedY = y;
        this.wantedZ = z;
    }

    public float getWantedX() {
        return this.level().isClientSide() ? this.entityData.get(WANTED_X) : this.wantedX;
    }
    public float getWantedY() {
        return this.level().isClientSide() ? this.entityData.get(WANTED_Y) : this.wantedY;
    }
    public float getWantedZ() {
        return this.level().isClientSide() ? this.entityData.get(WANTED_Z) : this.wantedZ;
    }

    @Override
    public void afterGotHit(LivingEntity entity) {

    }

    @Override
    public void tick() {
        super.tick();
        Entity entity = this.getOwner();
        if (!this.level().isClientSide()) {
            this.setFade(Mth.clamp((float) this.getLifeSpan() / this.getDelay(), 0.0F, 1.0F));
            if (!this.getFired()) {
                this.setFired(true);
            }
        }
        boolean flag = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir() && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();

                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
            this.clearFire();
        }

        if (this.getLifeSpan() >= this.getDuration()) {
            this.discard();
        }
        if (this.inGround) {
            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
            } else if (!this.level().isClientSide) {
                this.tickDespawn();
            }
        } else {
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            while(!this.isRemoved()) {
                if (entity instanceof Mob && ((Mob) entity).getTarget() != null) {
                    LivingEntity target = ((Mob) entity).getTarget();
                    this.setYRot(entity.getYRot());
                    this.setXRot(entity.getXRot());
                }
                if (entity instanceof Player player) {
                    LivingEntity target = (LivingEntity) RaycastUtil.getEntityLookedAt(player);
                    if (target != null) {
                        this.setYRot(entity.getYRot());
                        this.setXRot(entity.getXRot());
                    }
                }
                EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity1 = ((EntityHitResult) hitresult).getEntity();
                    Entity entity2 = this.getOwner();
                    if (entity1 instanceof Player && entity2 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity1)) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
                    switch (net.minecraftforge.event.ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {
                        case SKIP_ENTITY:
                            if (hitresult.getType() != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                                this.onHit(hitresult);
                                this.hasImpulse = true;
                                break;
                            }
                            ignoredEntities.add(entityhitresult.getEntity().getId());
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT_NO_DAMAGE:
                            this.discard();
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT:
                            this.setPierceLevel((byte) 0);
                        case DEFAULT:
                            this.onHit(hitresult);
                            this.hasImpulse = true;
                            break;
                    }
                }
                if (entityhitresult == null || this.getPierceLevel() <= 0) {
                    break;
                }
                hitresult = null;
            }
            if (this.isRemoved()) return;
            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            if (flag) {
                this.setYRot((float)(Mth.atan2(-d5, -d1) * (double)(180F / (float)Math.PI)));
            } else {
                this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
            }
            this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float motion = MOTION;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    float motionDrag = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * motionDrag, d2 - d6 * motionDrag, d3 - d1 * motionDrag, d5, d6, d1);
                }
                motion = this.getWaterInertia();
            }
            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale(motion));
            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();
            if (this.getLifeSpan() > this.getDelay()) {
                Entity owner = this.getOwner();
                if (entity instanceof Player player) {
                    LivingEntity target = (LivingEntity) RaycastUtil.getEntityLookedAt(player);
                    if (target != null) {
                        double dx = target.getX() - this.getX();
                        double dy = target.getY() + target.getBbHeight() * 0.5F - this.getY();
                        double dz = target.getZ() - this.getZ();
                        double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        dx /= d;
                        dy /= d;
                        dz /= d;
                        this.xPower = dx * this.getSpeed();
                        this.yPower = dy * this.getSpeed();
                        this.zPower = dz * this.getSpeed();
                    }
                }
                if (owner instanceof Mob && ((Mob) owner).getTarget() != null) {
                    LivingEntity target = ((Mob) owner).getTarget();
                    double dx = 0.0D;
                    double dy = 0.0D;
                    double dz = 0.0D;
                    if (target == null) {
                        dx = this.getWantedX() - this.getX();
                        dy = this.getWantedY() - this.getY();
                        dz = this.getWantedZ() - this.getZ();
                    } else {
                        dx = target.getX() - this.getX();
                        dy = target.getY() - target.getBbHeight() * 0.5F - this.getY();
                        dz = target.getZ() - this.getZ();
                    }

                    double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    dx /= d;
                    dy /= d;
                    dz /= d;
                    this.xPower = dx * this.getSpeed();
                    this.yPower = dy * this.getSpeed();
                    this.zPower = dz * this.getSpeed();
                }
            }
        }
    }
}