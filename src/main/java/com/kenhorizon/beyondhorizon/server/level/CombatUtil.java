package com.kenhorizon.beyondhorizon.server.level;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public class CombatUtil {

    public static float damageAtBack(float multiplier, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        float yaw = source.is(DamageTypes.MOB_PROJECTILE) ? -attacker.getYHeadRot() : attacker.getYHeadRot();
        float victimYaw = target.getYHeadRot();
        float difference = victimYaw - yaw;
        difference = posMod(difference + 180.0f, 360.0f) - 180.0f;
        boolean doBonusDamage = Math.abs(difference) <= 30.0f;
        return doBonusDamage ? damageDealt + (damageDealt * multiplier) : damageDealt;
    }

    private static float posMod(float num, float den) {
        return (num % den + den) % den;
    }
}
