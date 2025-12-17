package com.kenhorizon.beyondhorizon.server.entity.animation;

import net.minecraft.world.entity.AnimationState;

public class Animation extends AnimationState {
    private int animationId = 0;
    public Animation(int animationId) {
        this.animationId = animationId;
    }

    public static Animation create(int animationId) {
        return new Animation(animationId);
    }

    public int getAnimationId() {
        return animationId;
    }
}
