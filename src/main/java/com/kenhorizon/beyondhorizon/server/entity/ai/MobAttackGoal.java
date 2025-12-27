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
    protected final int animation;
    protected final int[] start;
    protected final int[] end;
    protected final float attackRange;
    protected final int seeTick;
    protected final int maxDuration;

    public MobAttackGoal(T entity, int animation, int[] start, int[] end, int seeTick, int maxDuration, boolean interrupt) {
        this.entity = entity;
        this.animation = animation;
        this.start = start;
        this.end = end;
        this.seeTick = seeTick;
        this.maxDuration = maxDuration;
        this.attackRange = (float) this.entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (interrupt) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }
    }
    public MobAttackGoal(T entity, int animation, int[] start, int[] end, int seeTick, int maxDuration) {
        this(entity, animation, start, end, seeTick, maxDuration, false);
    }

    public MobAttackGoal(T entity, int animation, int[] start, int end, int seeTick, int maxDuration) {
        this(entity, animation, start, new int[] {end}, seeTick, maxDuration, false);
    }

    public MobAttackGoal(T entity, int animation, int start, int[] end, int seeTick, int maxDuration) {
        this(entity, animation, new int[] {start}, end, seeTick, maxDuration, false);
    }

    public MobAttackGoal(T entity, int animation, int start, int end, int seeTick, int maxDuration) {
        this(entity, animation, new int[] {start}, new int[] {end}, seeTick, maxDuration, false);
    }

    @Override
    public void start() {
        super.start();
        int animationId = this.start[this.entity.getRandom().nextInt(this.start.length)];
        this.entity.setAnimation(animationId);
        BeyondHorizon.LOGGER.info("Start Animation ID: {}", animationId);
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        int animationId = this.end[this.entity.getRandom().nextInt(this.end.length)];
        this.entity.setAnimation(animationId);
        BeyondHorizon.LOGGER.info("End Animation ID: {}", animationId);
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
        return target != null && target.isAlive() && this.entity.distanceTo(target) < this.attackRange && this.entity.getAnimation() == this.animation;
    }

    @Override
    public boolean canContinueToUse() {
        for (int animation : this.start) {
            if (this.entity.getAnimation() == animation) {
                return this.entity.getAnimationTick() <= this.maxDuration;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if (this.entity.getAnimationTick() < this.seeTick && target != null) {
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
