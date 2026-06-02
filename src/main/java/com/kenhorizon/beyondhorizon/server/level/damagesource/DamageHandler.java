package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DamageHandler {
    public static DamageSource dealAdaptiveDamage(LivingEntity attacker, float damagedealt) {
        double AD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
        double AP = AttributeUtils.getBonus(attacker, BHAttributes.ABILITY_POWER.get());
        return AD > AP ? BHDamageTypes.physicalDamage(attacker, null) : BHDamageTypes.magicDamage(attacker, null);
    }

    public static boolean damage(LivingEntity target, boolean bypassIFrame, DamageSource source, DamageTags damageTags, float damageModifiers, float amount) {
        if (bypassIFrame) {
            target.invulnerableTime = 0;
        }
        switch (damageTags) {
            case SELF_MAX_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, maxHealth(attacker, amount, damageModifiers));
                }
            }
            case SELF_MISSING_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, missingHealth(attacker, amount, damageModifiers));
                }
            }
            case SELF_CURRENT_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, currentHealth(attacker, amount, damageModifiers));
                }
            }
            case TARGET_MAX_HEALTH -> {
                return target.hurt(source, maxHealth(target, amount, damageModifiers));
            }
            case TARGET_MISSING_HEALTH -> {
                return target.hurt(source, missingHealth(target, amount, damageModifiers));
            }
            case TARGET_CURRENT_HEALTH -> {
                return target.hurt(source, currentHealth(target, amount, damageModifiers));
            }
            case INSTANT_KILL -> {
                return target.hurt(BHDamageTypes.trueDamage(source.getEntity(), target), target.getMaxHealth() * 9999.99F);
            }
            default -> target.hurt(source, amount);
        }
        return target.hurt(source, amount);
    }

    public static boolean instantKill(LivingEntity target, DamageSource source) {
        return damage(target, BHDamageTypes.trueDamage(source.getEntity(), target), target.getMaxHealth(), DamageTags.INSTANT_KILL, target.getMaxHealth());
    }

    public static boolean damage(LivingEntity target, DamageSource source, float amount, DamageTags damageTags, float damageModifiers) {
        return damage(target, false, source, damageTags, damageModifiers, amount);
    }

    public static boolean damage(LivingEntity target, DamageSource source, float amount) {
        return damage(target, false, source, DamageTags.DEFAULT, 0, amount);
    }

    public static boolean damage(LivingEntity target, boolean bypassIFrame, DamageSource source, float amount) {
        return damage(target, bypassIFrame, source, DamageTags.DEFAULT, 0, amount);
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
        float damageCap = -1;
        return maxHealth(target, damageDealt, percentHealth, damageCap);
    }

    public static float maxHealth(LivingEntity target, float damageDealt, float percentHealth, float damageCap) {
        return damageCap > 0 ? Math.min(damageCap, additional(damageDealt,target.getMaxHealth() * percentHealth)) : additional(damageDealt,target.getMaxHealth() * percentHealth);
    }

    public static float currentHealth(LivingEntity target, float damageDealt, float percentHealth, float damageCap) {
        return damageCap > 0 ? Math.min(damageCap, additional(damageDealt, target.getHealth() * percentHealth)) : additional(damageDealt, target.getHealth() * percentHealth);
    }

    public static float currentHealth(LivingEntity target, float damageDealt, float percentHealth) {
        float damageCap = -1;
        return currentHealth(target, damageDealt, percentHealth, damageCap);
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