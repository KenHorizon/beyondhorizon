package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MobAttackGoal<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final boolean isLoop;
    protected final int animation;
    protected final int start;
    protected final int end;
    protected final float attackRange;
    protected final int seeTick;
    protected final int maxDuration;

    public MobAttackGoal(T entity, int animation, int start1, int end, int seeTick, int maxDuration, boolean isLoop, boolean interrupt) {
        BeyondHorizon.LOGGER.debug("Randomizing Value {}", entity.getRandom().nextBoolean() ? 1 : 2);
        this.entity = entity;
        this.isLoop = isLoop;
        this.animation = animation;
        this.start = start1;
        this.end = end;
        this.seeTick = seeTick;
        this.maxDuration = maxDuration;
        this.attackRange = (float) this.entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (interrupt) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }
    }
    public MobAttackGoal(T entity, int animation, int start, int end, int seeTick, int maxDuration, boolean isLoop) {
        this(entity, animation, start, end, seeTick, maxDuration, isLoop, false);
    }

    public MobAttackGoal(T entity, int animation, int start, int end, int seeTick, int maxDuration) {
        this(entity, animation, start, end, seeTick, maxDuration, false, false);
    }

    public MobAttackGoal(T entity, int animation, int start, int end, int seeTick) {
        this(entity, animation, start, end, seeTick,0, true, false);
    }
    public MobAttackGoal(T entity, int animation, int start, int end, int seeTick, boolean interrupt) {
        this(entity, animation, start, end, seeTick,0, true, interrupt);
    }


    @Override
    public void start() {
        super.start();
        this.entity.setAnimation(this.start);
        this.entity.getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimation(this.end);
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

    public void endAttackState() {
        this.entity.setAnimation(this.end);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.isLoop) {
            return true;
        } else {
            return this.entity.getAnimationTick() <= this.maxDuration && this.entity.getAnimation() == this.start;
        }
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        boolean flag = this.entity.getAnimationTick() < this.seeTick;
        if (target != null) {
            if (flag) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                this.entity.lookAt(target, 30.0F, 30.0F);
            } else {
                entity.getLookControl().setLookAt(target,0F, 30.0F);
                this.entity.setYRot(this.entity.yRotO);
            }
        }
        this.entity.getNavigation().stop();

    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
