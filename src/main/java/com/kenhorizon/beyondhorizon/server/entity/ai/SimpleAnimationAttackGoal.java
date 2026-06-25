package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.entity.util.AnimationTickers;

public class SimpleAnimationAttackGoal<T extends BHLibEntity> extends MobAttackGoal<T> {
    private AnimationTickers animationTickers;
    public SimpleAnimationAttackGoal(T entity, AnimationTickers animationTickers, int animation, int start, int end, int seeTick, int maxDuration, boolean isLoop, boolean interrupt) {
        super(entity, animation, start, end, seeTick, maxDuration, isLoop, interrupt);
        this.animationTickers = animationTickers;
    }

    public SimpleAnimationAttackGoal(T entity, AnimationTickers animationTickers, int animation, int start, int end, int seeTick, int maxDuration, boolean isLoop) {
        super(entity, animation, start, end, seeTick, maxDuration, isLoop);
        this.animationTickers = animationTickers;
    }

    public SimpleAnimationAttackGoal(T entity, AnimationTickers animationTickers, int animation, int start, int end, int seeTick, int maxDuration) {
        super(entity, animation, start, end, seeTick, maxDuration);
        this.animationTickers = animationTickers;
    }

    public SimpleAnimationAttackGoal(T entity, AnimationTickers animationTickers, int animation, int start, int end, int seeTick) {
        super(entity, animation, start, end, seeTick);
        this.animationTickers = animationTickers;
    }

    public SimpleAnimationAttackGoal(T entity, AnimationTickers animationTickers, int animation, int start, int end, int seeTick, boolean interrupt) {
        super(entity, animation, start, end, seeTick, interrupt);
        this.animationTickers = animationTickers;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.getAnimationTickers().isReadyToUse();
    }

    @Override
    public void stop() {
        this.getAnimationTickers().setCooldown();
        super.stop();
    }

    public AnimationTickers getAnimationTickers() {
        return animationTickers;
    }

    public void setAnimationTickers(AnimationTickers animationTickers) {
        this.animationTickers = animationTickers;
    }
}
