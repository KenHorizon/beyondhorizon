package com.kenhorizon.beyondhorizon.server.entity.util;

public class AnimationTickers {
    public int tick = 0;
    private final int cooldownDefault;

    public AnimationTickers(int cooldownDefault) {
        this.cooldownDefault = cooldownDefault;
    }

    public static AnimationTickers create(int cooldownDefault) {
        return new AnimationTickers(cooldownDefault);
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void setCooldown() {
        this.tick = this.cooldownDefault;
    }

    public void cooldownTick() {
        if (this.tick > 0) this.tick--;
    }

    public int getTick() {
        return tick;
    }

    public boolean isReadyToUse() {
        return this.tick <= 0;
    }
}
