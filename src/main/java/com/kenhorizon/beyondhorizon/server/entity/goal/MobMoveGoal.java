package com.kenhorizon.beyondhorizon.server.entity.goal;

import com.kenhorizon.beyondhorizon.server.entity.BHBaseEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class MobMoveGoal extends Goal {
    private final BHBaseEntity entity;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private int delayCounter;
    protected final double moveSpeed;

    public MobMoveGoal(BHBaseEntity boss, boolean followingTargetEvenIfNotSeen, double moveSpeed) {
        this.entity = boss;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.moveSpeed = moveSpeed;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive();
    }


    @Override
    public void stop() {
        entity.getNavigation().stop();
        LivingEntity livingentity = this.entity.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.entity.setTarget((LivingEntity) null);
        }
        this.entity.setAggressive(false);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.entity.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.entity.getNavigation().isDone();
        } else if (!this.entity.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
        }
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.path, this.moveSpeed);
        this.entity.setAggressive(true);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distSq = this.entity.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + this.entity.getRandom().nextInt(7);
                if (distSq > Math.pow(this.entity.getAttribute(Attributes.FOLLOW_RANGE).getValue(), 2.0D)) {
                    if (!this.entity.isPathFinding()) {
                        if (!this.entity.getNavigation().moveTo(target, 1.0D)) {
                            this.delayCounter += 5;
                        }
                    }
                } else {
                    this.entity.getNavigation().moveTo(target, this.moveSpeed);
                }
            }
        }
    }
}