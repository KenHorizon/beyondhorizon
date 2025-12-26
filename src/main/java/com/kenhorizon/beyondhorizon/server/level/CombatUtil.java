package com.kenhorizon.beyondhorizon.server.level;

import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public class CombatUtil {

    public static float multiplier(float damageDealt, float modifier) {
        return damageDealt * (1.0F + modifier);
    }

    public static float additional(float damageDealt, float additionalDamage) {
        return damageDealt + additionalDamage;
    }

    public static float missingHealth(LivingEntity entity, float damageDealt, float perPercentage) {
        return CombatUtil.multiplier(damageDealt, (float) Maths.perValue(entity.getHealth(), perPercentage, perPercentage));
    }

    public static float maxHealth(LivingEntity target, float damageDealt, float percentHealth) {
        return maxHealth(target, damageDealt, percentHealth, -1);
    }

    public static float maxHealth(LivingEntity target, float damageDealt, float percentHealth, float damageCap) {
        return damageCap != -1 ? Math.min(damageCap, additional(damageDealt,target.getMaxHealth() * percentHealth)) : additional(damageDealt,target.getMaxHealth() * percentHealth);
    }

    public static float currentHealth(LivingEntity target, float damageDealt, float percentHealth, float damageCap) {
        return damageCap > 0 ? Math.min(damageCap, additional(damageDealt, target.getHealth() * percentHealth)) : additional(damageDealt, target.getHealth() * percentHealth);
    }

    public static float currentHealth(LivingEntity target, float damageDealt, float percentHealth) {
        return currentHealth(target, damageDealt, percentHealth, -1);
    }

    public static float damageAtBack(float multiplier, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        float yaw = source.is(DamageTypes.MOB_PROJECTILE) ? -attacker.getYHeadRot() : attacker.getYHeadRot();
        float victimYaw = target.getYHeadRot();
        float difference = victimYaw - yaw;
        difference = posMod(difference + 180.0f, 360.0f) - 180.0f;
        boolean doBonusDamage = Math.abs(difference) <= 30.0f;
        return doBonusDamage ? multiplier(damageDealt, multiplier) : damageDealt;
    }

    private static float posMod(float num, float den) {
        return (num % den + den) % den;
    }
}
