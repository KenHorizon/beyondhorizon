package com.kenhorizon.beyondhorizon.server.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BossInactiveGoal extends Goal {
    private final boolean why;
    public BossInactiveGoal(boolean why) {
        this.why = why;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
    }
    @Override
    public boolean canUse() {
        return this.why;
    }
}