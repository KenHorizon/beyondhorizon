package com.kenhorizon.beyondhorizon.server.entity.goal;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class MobAttackGoal<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final int getAnimation;
    protected final int startAnimation;
    protected final int endAnimation;
    protected final float chanceToUse;
    protected final float attackRange;
    protected final int attackMaxTick;
    protected final int attackSeeTick;

    public MobAttackGoal(T entity, float chanceToUse, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, float attackRange, boolean interrupt) {
        this.entity = entity;
        this.chanceToUse = chanceToUse;
        this.getAnimation = getAnimation;
        this.startAnimation = startAnimation;
        this.endAnimation = endAnimation;
        this.attackSeeTick = attackSeeTick;
        this.attackMaxTick = attackMaxTick;
        this.attackRange = attackRange;
        if (interrupt) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }
    }
    public MobAttackGoal(T entity, float chanceToUse, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, float attackRange) {
        this(entity, chanceToUse, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick, attackRange, false);
    }
    public MobAttackGoal(T entity, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, float attackRange) {
        this(entity, 1.0F, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick, attackRange, false);
    }
    @Override
    public void start() {
        super.start();
        this.entity.setAnimation(this.startAnimation);
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimation(this.endAnimation);
        LivingEntity target = this.entity.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.entity.setTarget(null);
        }
        this.entity.getNavigation().stop();
        if (target == null) {
            this.entity.setAggressive(false);
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive() && this.entity.distanceTo(target) < this.attackRange && this.entity.getAnimation() == this.getAnimation;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getAnimation() == this.startAnimation && this.entity.getAnimationTick() <= this.attackMaxTick;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if (this.entity.getAnimationTick() < this.attackSeeTick && target != null) {
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.entity.lookAt(target, 30.0F, 30.0F);
        } else {
            this.entity.setYRot(this.entity.yRotO);
        }
        this.entity.getNavigation().stop();

    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
