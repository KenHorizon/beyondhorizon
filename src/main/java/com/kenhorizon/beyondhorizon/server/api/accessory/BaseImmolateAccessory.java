package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class BaseImmolateAccessory extends AbstractImmolateAccessory {

    public BaseImmolateAccessory(float magnitude) {
        super(magnitude);
    }

    @Override
    public float getImmolateDamage(LivingEntity affected, LivingEntity source) {
        return this.getMagnitude();
    }

    public static class ImmolateInfernoHeart extends AbstractImmolateAccessory {

        public ImmolateInfernoHeart(float magnitude) {
            super(magnitude);
        }

        @Override
        public float getImmolateDamage(LivingEntity affected, LivingEntity source) {
            return (float) (source.getMaxHealth() * this.getMagnitude());
        }
    }
    public static class ImmolateVoid extends AbstractImmolateAccessory {

        public ImmolateVoid(float magnitude) {
            super(magnitude);
        }

        @Override
        public DamageSource getSource(LivingEntity affected) {
            return BHDamageTypes.trueDamage(affected, true);
        }

        @Override
        public float getImmolateDamage(LivingEntity affected, LivingEntity source) {
            return (float) (source.getMaxHealth() * this.getMagnitude());
        }
    }
}
