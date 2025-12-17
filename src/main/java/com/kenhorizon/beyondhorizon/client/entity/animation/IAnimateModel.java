package com.kenhorizon.beyondhorizon.client.entity.animation;

import net.minecraft.world.entity.AnimationState;

public interface IAnimateModel {

    void setAnimation(AnimationState animationState);

    AnimationState getCurrentAnimation();

    void endAnimation(AnimationState animationState);

    void setAnimationTick(int duration);

    int getAnimationTick();

    AnimationState[] getAnimations();
    
}
