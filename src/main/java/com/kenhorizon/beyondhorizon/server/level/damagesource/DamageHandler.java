package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DamageHandler {
    public static boolean damage(LivingEntity target, boolean bypassIFrame, DamageSource source, float amount) {
        if (bypassIFrame) {
            target.invulnerableTime = 0;
        }
        return target.hurt(source, amount);
    }
    public static boolean damage(LivingEntity target, DamageSource source, float amount) {
        return damage(target, false, source, amount);
    }
}