package com.kenhorizon.beyondhorizon.server.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class SmartMobGoal<T extends Mob> extends Goal {
    public final T entity;
    public final double distanceToAvoid;
    public SmartMobGoal(T entity, double distanceToAvoid) {
        this.entity = entity;
        this.distanceToAvoid = distanceToAvoid;
    }


    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            double distance = this.entity.distanceTo(target);
            if (distance < this.distanceToAvoid) {
                this.entity.getNavigation().moveTo(this.entity.getX() - target.getX(), this.entity.getY(), this.entity.getZ() - target.getZ(), 1.2F);
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
