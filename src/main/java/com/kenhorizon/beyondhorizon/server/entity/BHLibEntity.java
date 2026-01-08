package com.kenhorizon.beyondhorizon.server.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BHLibEntity extends BHBaseEntity implements IEntityDamageCap {
    public AnimationState idleAnimation = new AnimationState();
    public static final int ID_ANIMATION_EMPTY = 0;
    private int idleTime;
    private float damageCap = -1;
    private int animationTick;
    public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(BHLibEntity.class, EntityDataSerializers.INT);

    public BHLibEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public void setExp(int xpPoints) {
        this.xpReward = xpPoints;
    }

    public void setAnimationTick(int animationTick) {
        this.animationTick = animationTick;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_STATE, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.idleTime++;
            if (this.idleTime >= this.getIdleTime()) {
                this.idleAnimation.startIfStopped(this.tickCount);
                this.idleTime = 0;
            }
        }
        if (this.getAnimation() > 0) {
            this.setAnimationTick(this.getAnimationTick() + 1);
        }
    }

    public int getIdleTime() {
        return 20;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.setAnimationTick(0);
        } else {
            super.handleEntityEvent(id);
        }
    }


    public void setDamageCap(float damageCap) {
        this.damageCap = damageCap;
    }

    @Override
    public float getDamageCap() {
        return this.damageCap;
    }

    public boolean allowDamageCap() {
        return this.getDamageCap() > 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = source.is(DamageTypes.GENERIC) || source.is(DamageTypes.GENERIC_KILL);
        if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && this.allowDamageCap()) {
            amount = Math.min(this.getDamageCap(), amount);
        }
        return super.hurt(source, amount);
    }

    public void setAnimation(int animation) {
        this.setAnimationTick(0);
        this.entityData.set(ANIMATION_STATE, animation);
        this.level().broadcastEntityEvent(this, (byte) -animation);
    }

    public int getAnimation() {
        return this.entityData.get(ANIMATION_STATE);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        this.setAnimation(this.getAnimationDeath());
    }

    public void doDodge(int chance) {
        this.doDodge(chance, 0);
    }

    public void doDodge(int chance, int animationId) {
        if (this.getRandomChances(chance)) return;
        this.performDodge(animationId);
    }

    public void doDodge(float chance) {
        this.doDodge(chance, 0);
    }

    public void doDodge(float chance, int animationId) {
        if (this.getRandomChances(chance)) return;
        this.performDodge(animationId);
    }

    private void performDodge(int animationId) {
        List<Projectile> projectilesNearby = this.getEntitiesNearby(Projectile.class, 30);
        for (Projectile projectile : projectilesNearby) {
            Vec3 aActualMotion = new Vec3(projectile.getX() - projectile.xo, projectile.getY() - projectile.yo, projectile.getZ() - projectile.zo);
            if (aActualMotion.length() < 0.1 || projectile.tickCount <= 1) {
                continue;
            }
            float dot = (float) projectile.getDeltaMovement().normalize().dot(this.position().subtract(projectile.position()).normalize());
            if (dot > 0.96) {
                Vec3 dodgeVec = projectile.getDeltaMovement().cross(new Vec3(0, 1, 0)).normalize().scale(1.2);
                Vec3 newPosLeft = position().add(dodgeVec.scale(2));
                Vec3 newPosRight = position().add(dodgeVec.scale(-2));
                Vec3 diffLeft = newPosLeft.subtract(projectile.position());
                Vec3 diffRight = newPosRight.subtract(projectile.position());
                if (diffRight.dot(projectile.getDeltaMovement()) > diffLeft.dot(projectile.getDeltaMovement())) {
                    dodgeVec = dodgeVec.scale(-1);
                }
                this.setDeltaMovement(getDeltaMovement().add(dodgeVec));
                this.setAnimation(animationId);
            }
        }
    }

    public boolean inBetweenHealth(float to, float from) {
        return this.getHealthRatio() >= from && this.getHealthRatio() <= to;
    }

    public boolean getAnimationState(int id) {
        return id == this.getAnimation();
    }


    protected boolean shouldFollowUp(float Range) {
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            Vec3 targetMoveVec = target.getDeltaMovement();
            Vec3 betweenEntitiesVec = this.position().subtract(target.position());
            boolean targetComingCloser = targetMoveVec.dot(betweenEntitiesVec) > 0;
            return this.distanceTo(target) < Range || (this.distanceTo(target) < 5 + Range && targetComingCloser);
        }
        return false;
    }

    public AnimationState[] getAnimations() {
        return new AnimationState[0];
    }
}
