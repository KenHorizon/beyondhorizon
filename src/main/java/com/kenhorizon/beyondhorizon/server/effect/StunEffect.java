package com.kenhorizon.beyondhorizon.server.effect;

import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class StunEffect extends BHMobEffect {
    public StunEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().random.nextFloat() < entity.getBbWidth() * 0.12F) {
            entity.level().addParticle(BHParticle.STUN_PARTICLES.get(), entity.getX(), entity.getEyeY(), entity.getZ(), entity.getId(), entity.level().random.nextFloat() * 360, 0);
        }
        if (entity.getDeltaMovement().y > 0) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0.1D, 1));
        }
        if (entity.level() instanceof ServerLevel sLevel) {
            sLevel.sendParticles(ParticleTypes.CRIT, entity.getRandomX(0.5D), entity.getEyeY() + 0.50D, entity.getRandomZ(0.5D), 3, 0, 0, 0, 0.0D);
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
