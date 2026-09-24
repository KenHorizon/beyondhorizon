package com.kenhorizon.beyondhorizon.server.level.entity.ai;

import com.kenhorizon.beyondhorizon.server.level.entity.boss.pyrolliger.Pyrolliger;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PyrolligerMoveGoal extends Goal {
    private final Pyrolliger entity;
    private Path path;
    private boolean followTargetEvenIfNotSeen;
    private int delayCounter;
    protected final double sprintSpeed;
    protected final double walkSpeed;

    protected final PathNavigation pathNav;

    public PyrolligerMoveGoal(Pyrolliger boss, boolean followTargetEvenIfNotSeen, double sprintSpeed, double walkSpeed) {
        this.entity = boss;
        this.pathNav = boss.getNavigation();
        this.sprintSpeed = sprintSpeed;
        this.walkSpeed = walkSpeed;
        this.followTargetEvenIfNotSeen = followTargetEvenIfNotSeen;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    public PyrolligerMoveGoal(Pyrolliger boss, double sprintSpeed, double walkSpeed) {
        this(boss, false, sprintSpeed, walkSpeed);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.path = this.entity.getNavigation().createPath(target, 0);
            return target.isAlive();
        }
        return false;
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.path, 1.0F);
        this.entity.setAggressive(true);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.entity.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.followTargetEvenIfNotSeen) {
            return !this.entity.getNavigation().isDone();
        } else if (!this.entity.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
        }
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
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.entity.setRunning(this.entity.distanceToSqr(target) < 49.0D);
            if (this.entity.distanceToSqr(target) < 49.0D) {
                this.entity.getNavigation().setSpeedModifier(this.sprintSpeed);
            } else {
                this.entity.getNavigation().setSpeedModifier(this.walkSpeed);
            }
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distSq = this.entity.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
            if (--this.delayCounter <= 0) {
                if (this.entity.isMelee()) {
                    this.delayCounter = 4 + this.entity.getRandom().nextInt(7);
                    if (distSq > Math.pow(this.entity.getAttribute(Attributes.FOLLOW_RANGE).getValue(), 2.0D)) {
                        if (!this.entity.isPathFinding()) {
                            if (!this.entity.getNavigation().moveTo(target, 1.0D)) {
                                this.delayCounter += 5;
                            }
                        }
                    } else {
                        this.entity.getNavigation().moveTo(target, this.walkSpeed);
                    }
                } else {
                    Vec3 vec3 = DefaultRandomPos.getPosAway(this.entity, 16, 7, target.position());
                    if (vec3 != null && !(target.distanceToSqr(vec3.x, vec3.y, vec3.z) < target.distanceToSqr(this.entity))) {
                        this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                        this.delayCounter += 5;
                    }
                }
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
