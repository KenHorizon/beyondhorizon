package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.CombatUtil;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DamageHandler {
    public static boolean damage(LivingEntity target, boolean bypassIFrame, DamageSource source, DamageTypes damageTypes, float damageModifiers, float amount) {
        if (bypassIFrame) {
            target.invulnerableTime = 0;
        }
        switch (damageTypes) {
            case SELF_MAX_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, CombatUtil.maxHealth(attacker, amount, damageModifiers));
                }
            }
            case SELF_MISSING_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, CombatUtil.missingHealth(attacker, amount, damageModifiers));
                }
            }
            case SELF_CURRENT_HEALTH -> {
                if (source.getEntity() instanceof LivingEntity attacker) {
                    return target.hurt(source, CombatUtil.currentHealth(attacker, amount, damageModifiers));
                }
            }
            case TARGET_MAX_HEALTH -> {
                return target.hurt(source, CombatUtil.maxHealth(target, amount, damageModifiers));
            }
            case TARGET_MISSING_HEALTH -> {
                return target.hurt(source, CombatUtil.missingHealth(target, amount, damageModifiers));
            }
            case TARGET_CURRENT_HEALTH -> {
                return target.hurt(source, CombatUtil.currentHealth(target, amount, damageModifiers));
            }
            case INSTANT_KILL -> {
                return target.hurt(source, target.getMaxHealth() * 9999.99F);
            }
        }
        return target.hurt(source, amount);
    }
    public static boolean instantKill(LivingEntity target, DamageSource source) {
        return damage(target, true, source, target.getMaxHealth());
    }
    public static boolean damage(LivingEntity target, DamageSource source, float amount, DamageTypes damageTypes, float damageModifiers) {
        return damage(target, false, source, damageTypes, damageModifiers, amount);
    }
    public static boolean damage(LivingEntity target, DamageSource source, float amount) {
        return damage(target, false, source, DamageTypes.DEFAULT, 0, amount);
    }
    public static boolean damage(LivingEntity target, boolean bypassIFrame, DamageSource source, float amount) {
        return damage(target, bypassIFrame, source, DamageTypes.DEFAULT, 0, amount);
    }
}