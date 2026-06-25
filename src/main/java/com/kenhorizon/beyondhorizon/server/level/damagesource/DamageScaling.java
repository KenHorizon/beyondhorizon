package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum DamageScaling {
    NONE,
    MISSING_HEALTH,
    CURRENT_HEALTH,
    MAX_HEALTH;

    public float CurentHP(LivingEntity entity, float scaling) {
        return entity.getHealth() * scaling;
    }

    public float MaxHP(LivingEntity entity, float scaling) {
        return entity.getMaxHealth() * scaling;
    }

    public float BonusHP(LivingEntity entity, float scaling) {
        return (float) (AttributeUtils.getBonus(entity, Attributes.MAX_HEALTH) * scaling);
    }

    public float MissingHP(LivingEntity entity, float percent, float bonus) {
        float ratioPoints = (entity.getMaxHealth() - entity.getHealth()) / entity.getMaxHealth();
        return (ratioPoints / percent) * bonus;
    }

    public float MissingHP(LivingEntity entity, float bonus) {
        return MissingHP(entity, 1.0F, bonus);
    }
}
