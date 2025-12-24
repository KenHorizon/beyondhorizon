package com.kenhorizon.beyondhorizon.server.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class BHLibEntity extends BHBaseEntity {
    private AnimationState currentAnimation;
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
        if (this.getAnimation() > 0) {
            this.setAnimationTick(this.getAnimationTick() + 1);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.setAnimationTick(0);
        } else {
            super.handleEntityEvent(id);
        }
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

    public AnimationState[] getAnimations() {
        return new AnimationState[0];
    }
}
