package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfoTypes;
import net.minecraft.world.entity.LivingEntity;

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
        public DamageInfoTypes damageType() {
            return DamageInfoTypes.TRUE_DAMAGE;
        }

        @Override
        public float getImmolateDamage(LivingEntity affected, LivingEntity source) {
            return (float) (source.getMaxHealth() * this.getMagnitude());
        }
    }
}
