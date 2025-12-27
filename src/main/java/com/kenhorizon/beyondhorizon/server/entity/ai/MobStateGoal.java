package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MobStateGoal<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final int start;
    protected final int end;
    protected final int animation;
    protected final int maxDuration;
    protected final int seeTick;

    public MobStateGoal(T entity, int getAnimation, int start, int end, int seeTick, int maxDuration) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        this.animation = getAnimation;
        this.start = start;
        this.end = end;
        this.seeTick = seeTick;
        this.maxDuration = maxDuration;
    }

    @Override
    public void start() {
        super.start();
        if (this.start != this.animation) {
            this.entity.setAnimation(this.start);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimation(this.end);
    }

    @Override
    public boolean canUse() {
        return this.entity.getAnimation() == this.animation;
    }

    @Override
    public boolean canContinueToUse() {
        return this.maxDuration > 0 ? this.entity.getAnimationTick() <= this.maxDuration && this.entity.getAnimation() == this.start : canUse();
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
