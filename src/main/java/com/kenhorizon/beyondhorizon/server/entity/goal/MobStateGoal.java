package com.kenhorizon.beyondhorizon.server.entity.goal;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MobStateGoal<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final int startAnimation;
    protected final int endAnimation;
    protected final int getAnimation;
    protected final int attackMaxTick;
    protected final int attackSeeTick;

    public MobStateGoal(T entity, int getAnimation, int startAnimation, int endAnimation, int attackSeeTick, int attackMaxTick) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        this.getAnimation = getAnimation;
        this.startAnimation = startAnimation;
        this.endAnimation = endAnimation;
        this.attackSeeTick = attackSeeTick;
        this.attackMaxTick = attackMaxTick;
    }

    @Override
    public void start() {
        super.start();
        if (this.startAnimation != this.getAnimation) {
            this.entity.setAnimation(this.startAnimation);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimation(this.endAnimation);
    }

    @Override
    public boolean canUse() {
        return this.entity.getAnimation() == this.getAnimation;
    }

    @Override
    public boolean canContinueToUse() {
        return this.attackMaxTick > 0 ? this.entity.getAnimationTick() <= this.attackMaxTick && this.entity.getAnimation() == this.startAnimation : canUse();
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
