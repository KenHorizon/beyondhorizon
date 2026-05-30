package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.server.entity.summoned.BHSummonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class SummonedAttackGoal extends Goal {
    private final BHSummonEntity summonEntity;
    private LivingEntity owner;
    private final Level level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float startDistance;
    private float oldWaterCost;

    public SummonedAttackGoal(BHSummonEntity summonEntity, double speedModifier, float distance) {
        this.summonEntity = summonEntity;
        this.level = summonEntity.level();
        this.speedModifier = speedModifier;
        this.navigation = summonEntity.getNavigation();
        this.startDistance = distance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(summonEntity.getNavigation() instanceof GroundPathNavigation) && !(summonEntity.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.summonEntity.getUsingEntity();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else {
            return !(this.summonEntity.distanceToSqr(this.owner) <= (double)(this.startDistance * this.startDistance));
        }
    }

    private boolean unableToMove() {
        return this.summonEntity.isPassenger() || this.summonEntity.isLeashed();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.summonEntity.getPathfindingMalus(BlockPathTypes.WATER);
        this.summonEntity.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.summonEntity.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        var owner = this.summonEntity.getUsingEntity();
        var allEntity = this.summonEntity.getEntityLivingBaseNearby(owner.getX(), owner.getY(), owner.getZ(), this.startDistance);

    }
}
