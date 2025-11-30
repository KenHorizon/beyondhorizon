package com.kenhorizon.beyondhorizon.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RootedEffect extends MobEffect {
    public RootedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getDeltaMovement().y > 0) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0.1D, 1));
        }
        if (entity instanceof Mob mob) {
            entity.setXRot(30.0F);
            entity.xRotO = 30.0F;
            if (!mob.level().isClientSide) {
                mob.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
                mob.goalSelector.setControlFlag(Goal.Flag.JUMP, false);
                mob.goalSelector.setControlFlag(Goal.Flag.LOOK, false);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
