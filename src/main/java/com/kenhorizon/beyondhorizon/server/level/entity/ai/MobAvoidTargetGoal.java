package com.kenhorizon.beyondhorizon.server.level.entity.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class MobAvoidTargetGoal<T extends LivingEntity> extends Goal {
    protected final PathfinderMob entity;
    protected final double speedModifier;
    protected final double maxDistance;
    protected Path path;
    protected final PathNavigation pathNav;
    public MobAvoidTargetGoal(PathfinderMob entity, float maxDistance, double speedModifier) {
        this.entity = entity;
        this.maxDistance = maxDistance;
        this.speedModifier = speedModifier;
        this.pathNav = entity.getNavigation();
    }
    
    @Override
    public void start() {
        this.pathNav.moveTo(this.path, this.speedModifier);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            Vec3 vec3 = DefaultRandomPos.getPosAway(this.entity, 16, 7, target.position());
            if (vec3 == null) {
                return false;
            } else if (target.distanceToSqr(vec3.x, vec3.y, vec3.z) < target.distanceToSqr(this.entity)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            if (this.entity.distanceToSqr(target) < 49.0D) {
                this.entity.getNavigation().setSpeedModifier(this.speedModifier);
            } else {
                this.entity.getNavigation().setSpeedModifier(this.speedModifier / 2.0D);
            }
        }
    }
}