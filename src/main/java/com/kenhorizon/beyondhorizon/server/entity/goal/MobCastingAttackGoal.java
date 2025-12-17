package com.kenhorizon.beyondhorizon.server.entity.goal;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.animation.Animation;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class MobCastingAttackGoal<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final Animation startAnimation;
    protected final Animation endAnimation;
    protected final float chanceToUse;
    protected final float attackRange;
    protected final int attackMaxTick;
    protected final int attackSeeTick;
    protected int attackCooldown;

    public MobCastingAttackGoal(T entity, float chanceToUse, Animation startAnimation, Animation endAnimation, int attackSeeTick, int attackMaxTick, float attackRange, boolean interrupt) {
        this.entity = entity;
        this.chanceToUse = chanceToUse;
        this.startAnimation = startAnimation;
        this.endAnimation = endAnimation;
        this.attackSeeTick = attackSeeTick;
        this.attackMaxTick = attackMaxTick;
        this.attackRange = attackRange;
        if (interrupt) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }
    }

    @Override
    public void start() {
        super.start();
        this.entity.setAttackState(this.startAnimation.getAnimationId());
        this.attackCooldown = this.entity.tickCount + this.getAttackCooldown();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAttackState(this.endAnimation.getAnimationId());
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
        boolean canUse = target != null && target.isAlive() && this.entity.getRandom().nextFloat() <= this.chanceToUse && this.entity.distanceTo(target) < this.attackRange && this.entity.getAttackState() == this.startAnimation.getAnimationId();
        return canUse && this.entity.tickCount >= this.attackCooldown;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getAttackState() == this.startAnimation.getAnimationId() && this.entity.getAnimationTick() <= this.attackMaxTick;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if (this.entity.getAnimationTick() < this.mob && target != null) {
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

    protected int getAttackDelay() {
        return 20;
    }

    protected abstract int getAttackCooldown();
}
