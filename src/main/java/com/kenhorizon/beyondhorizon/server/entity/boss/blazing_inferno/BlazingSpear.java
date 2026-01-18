package com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno;


import com.kenhorizon.beyondhorizon.server.entity.projectiles.ExtendedProjectile;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.server.util.RaycastUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class BlazingSpear extends ExtendedProjectile {
    public BlazingSpear(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setDuration(160);
        this.setIgniteAttack(true);
    }

    public BlazingSpear(Level level, LivingEntity shooter) {
        this(BHEntity.BLAZING_SPEAR.get(), level);
        this.setOwner(shooter);
    }

    @Override
    public void afterGotHit(LivingEntity entity) {
        entity.invulnerableTime = 0;
        entity.addEffect(new MobEffectInstance(BHEffects.ROOTED.get(), Maths.sec(2)));
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
            this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
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
                    Entity entity1 = ((EntityHitResult)hitresult).getEntity();
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

            if (this.isRemoved())
                return;

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
            float motion = 0.99F;
            float motionValue = 0.05F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    float motionDrag = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * motionDrag, d2 - d6 * motionDrag, d3 - d1 * motionDrag, d5, d6, d1);
                }
                motion = this.getWaterInertia();
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double) motion));

            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();

            if (this.getLifeSpan() == this.getDelay()) {
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
                if (entity instanceof Mob && ((Mob) entity).getTarget() != null) {
                    LivingEntity target = ((Mob) entity).getTarget();
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
        }
    }
}