package com.kenhorizon.libs.server.animation;

import com.kenhorizon.beyondhorizon.server.world.entity.util.AnimationTickers;
import net.minecraft.world.entity.AnimationState;

public final class AnimationManager {
    private final int id;
    private final AnimationState anim;
    private final AnimationTickers animTick;

    private AnimationManager(int id, AnimationState anim, AnimationTickers animTick) {
        this.id = id;
        this.anim = anim;
        this.animTick = animTick;
    }

    public static AnimationManager create(int id, AnimationState anim, AnimationTickers tick) {
        return new AnimationManager(id, anim, tick);
    }

    public int getId() {
        return id;
    }

    public AnimationState getAnimationState() {
        return anim;
    }

    public AnimationTickers getAnimationTick() {
        return animTick;
    }
}
