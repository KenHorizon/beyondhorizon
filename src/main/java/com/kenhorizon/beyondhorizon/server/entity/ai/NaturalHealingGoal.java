package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.server.entity.BHBaseEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class NaturalHealingGoal extends Goal {
    protected int timeCooldown;
    protected float maxHealthRecovery;
    protected int defaultTimeCooldown;
    public static int COOLDOWN = 200;
    protected BHBaseEntity entity;

    public NaturalHealingGoal(BHBaseEntity entity, int cooldown, float maxHealthRecovery) {
        this.entity = entity;
        this.defaultTimeCooldown = cooldown;
        this.maxHealthRecovery = maxHealthRecovery;
    }
    public NaturalHealingGoal(BHBaseEntity entity) {
        this(entity, COOLDOWN, 0.10F);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (this.timeCooldown > 0) this.timeCooldown --;
        if (target != null) {
            this.timeCooldown = defaultTimeCooldown;
        }
        if (this.timeCooldown <= 0) {
            if (!this.entity.isNoAi()) {
                if (this.entity.tickCount % 10 == 0) {
                    this.entity.heal(this.entity.getMaxHealth() * this.maxHealthRecovery);
                }
            }
        }
    }
}