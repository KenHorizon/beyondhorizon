package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public class DamageHandler {
    public static boolean damage(LivingEntity target, boolean bypassIFrame, DamageSource source, DamageScaling damageScaling, float damageModifiers, float amount) {
        if (bypassIFrame) {
            target.invulnerableTime = 0;
        }
        switch (damageScaling) {
            case MAX_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, maxHealth(target, amount, damageModifiers));
                }
            }
            case MISSING_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, missingHealth(target, amount, damageModifiers));
                }
            }
            case CURRENT_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, currentHealth(target, amount, damageModifiers));
                }
            }
            default -> target.hurt(source, amount);
        }
        return target.hurt(source, amount);
    }

    public static boolean damage(LivingEntity target, DamageSource source, float amount, DamageScaling damageScaling, float damageModifiers) {
        return damage(target, false, source, damageScaling, damageModifiers, amount);
    }

    public static float multiplier(float damageDealt, float modifier) {
        return damageDealt * (1.0F + modifier);
    }

    public static float additional(float damageDealt, float additionalDamage) {
        return damageDealt + additionalDamage;
    }

    public static float missingHealth(LivingEntity entity, float damageDealt, float bonus) {
        return missingHealth(entity, damageDealt, 1.0F, bonus);
    }

    public static float missingHealth(LivingEntity entity, float damageDealt, float percent, float bonus) {
        float missingHealth = (entity.getMaxHealth() - entity.getHealth()) / entity.getMaxHealth();
        float outputDamage = (missingHealth / percent) * bonus;
        return multiplier(damageDealt, outputDamage);
    }

    public static float maxHealth(LivingEntity target, float damageDealt, float percentHealth) {
        float maxHealth = target.getMaxHealth() * percentHealth;
        return additional(damageDealt, maxHealth);
    }

    public static float currentHealth(LivingEntity target, float damageDealt, float percentHealth) {
        return additional(damageDealt, target.getHealth() * percentHealth);
    }

    public static float damageAtBack(float multiplier, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        float yaw = source.is(DamageTypes.MOB_PROJECTILE) ? -attacker.getYHeadRot() : attacker.getYHeadRot();
        float victimYaw = target.getYHeadRot();
        float difference = victimYaw - yaw;
        difference = posMod(difference + 180.0f, 360.0f) - 180.0f;
        boolean doBonusDamage = Math.abs(difference) <= 30.0f;
        if (doBonusDamage) {
            return multiplier(damageDealt, multiplier);
        } else {
            return damageDealt;
        }
    }

    private static float posMod(float num, float den) {
        return (num % den + den) % den;
    }
}