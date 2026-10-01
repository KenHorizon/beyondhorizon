package com.kenhorizon.beyondhorizon.server.world.entity.ai;

import com.kenhorizon.beyondhorizon.server.world.entity.ILinkedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class CopyOwnerTargetGoal<T extends LivingEntity> extends Goal {
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private T entity;
    public CopyOwnerTargetGoal(T entity) {
        this.entity = entity;
    }

    @Override
    public void start() {
        if (this.entity instanceof ILinkedEntity linkedEntity) {
            LivingEntity owner = linkedEntity.getOwner();
            if (this.entity instanceof PathfinderMob mob && owner instanceof PathfinderMob mobs) {
                mob.setTarget(mobs.getTarget());
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean canUse() {
        if (this.entity instanceof ILinkedEntity linkedEntity) {
            LivingEntity owner = linkedEntity.getOwner();
            if (owner instanceof Mob mob) {
                return mob.getTarget() != null && owner.canAttack(mob.getTarget(), this.copyOwnerTargeting);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
