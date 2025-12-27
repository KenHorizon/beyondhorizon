package com.kenhorizon.beyondhorizon.server.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class BHLibEntity extends BHBaseEntity {
    public AnimationState idleAnimation = new AnimationState();
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

    public float getDamageCap() {
        return this.damageCap;
    }

    public boolean allowDamageCap() {
        return this.getDamageCap() > 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
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
        if (this.getAnimationDeath() == -1) {
            this.setAnimation(this.getAnimationDeath());
        }
    }

    public boolean inBetweenHealth(float to, float from) {
        return this.getHealthRatio() >= from && this.getHealthRatio() <= to;
    }

    public boolean getAnimationState(int id) {
        return id == this.getAnimation();
    }


    public AnimationState[] getAnimations() {
        return new AnimationState[0];
    }
}
