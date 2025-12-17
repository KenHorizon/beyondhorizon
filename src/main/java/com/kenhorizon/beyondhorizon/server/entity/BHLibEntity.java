package com.kenhorizon.beyondhorizon.server.entity;

import com.kenhorizon.beyondhorizon.client.entity.animation.IAnimateModel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class BHLibEntity extends BHBaseEntity {
    private AnimationState currentAnimation;
    private int animationTick;
    public static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(BHLibEntity.class, EntityDataSerializers.INT);

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
        this.entityData.define(ATTACK_STATE, 0);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.animationTick = 0;
        }else {
            super.handleEntityEvent(id);
        }
    }

    public void setAttackState(int attackState) {
        this.animationTick = 0;
        this.entityData.set(ATTACK_STATE, attackState);
        this.level().broadcastEntityEvent(this, (byte) -attackState);
    }

    public int getAttackState() {
        return this.entityData.get(ATTACK_STATE);
    }
}
