package com.kenhorizon.beyondhorizon.server.entity.ai;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class UseAttacksAi<T extends BHLibEntity> extends Goal {
    protected final T entity;
    protected final int animation;
    protected final int start;

    public UseAttacksAi(T entity, int animation, int... start) {
        this.entity = entity;
        this.animation = animation;
        this.start = start[entity.getRandom().nextInt(start.length)];
    }

    @Override
    public void start() {
        BeyondHorizon.LOGGER.debug("Start Animation Id: {}", this.start);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean canUse() {
        return this.animation == entity.getAnimation();
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.entity.getNavigation().stop();
    }
}
