package com.kenhorizon.libs.server.world;

public class CooldownInstance {
    private int cooldownRemaining;
    private final int cooldown;

    public CooldownInstance(int cooldown) {
        this.cooldown = cooldown;
        this.cooldownRemaining = cooldown;
    }

    public CooldownInstance(int cooldown, int cooldownRemaining) {
        this.cooldown = cooldown;
        this.cooldownRemaining = cooldownRemaining;
    }

    public void decrement() {
        cooldownRemaining--;
    }

    public void decrementBy(int amount) {
        cooldownRemaining -= amount;
    }

    public int getCooldownRemaining() {
        return cooldownRemaining;
    }

    public int getCooldown() {
        return cooldown;
    }

    public float getCooldownPercent() {
        if (cooldownRemaining == 0) {
            return 0;
        }

        return cooldownRemaining / (float) cooldown;
    }
}
