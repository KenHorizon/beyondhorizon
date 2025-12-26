package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class MobAttackGoal<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final int getAnimation;
    protected final int[] startAnimation;
    protected final int endAnimation;
    protected final float attackRange;
    protected final int attackMaxTick;
    protected final int attackSeeTick;

    public MobAttackGoal(T entity, int getAnimation, int[] startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick, boolean interrupt) {
        this.entity = entity;
        this.getAnimation = getAnimation;
        this.startAnimation = startAnimation;
        this.endAnimation = endAnimation;
        this.attackSeeTick = attackSeeTick;
        this.attackMaxTick = attackMaxTick;
        this.attackRange = (float) this.entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (interrupt) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }
    }
    public MobAttackGoal(T entity, int getAnimation, int[] startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick) {
        this(entity, getAnimation, startAnimation, endAnimation, attackSeeTick, attackMaxTick, false);
    }

    public MobAttackGoal(T entity, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick) {
        this(entity, getAnimation, new int[] {startAnimation}, endAnimation, attackSeeTick, attackMaxTick, false);
    }

    @Override
    public void start() {
        super.start();
        this.entity.setAnimation(this.startAnimation[this.entity.getRandom().nextInt(this.startAnimation.length)]);
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimation(this.endAnimation);
        BeyondHorizon.LOGGER.info("Animation Id Used: {}", this.entity.getAnimation());
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
        for (int animationTick : this.startAnimation) {
            if (this.entity.getAnimation() == animationTick) {
                return this.entity.getAnimationTick() <= this.attackMaxTick;
            }
        }
        return false;
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
