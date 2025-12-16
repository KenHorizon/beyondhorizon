package com.kenhorizon.beyondhorizon.server.api.skills;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ExecuteDamageSkill extends WeaponSkills {
    public ExecuteDamageSkill(float magnitude) {
        this.setMagnitude(magnitude);
    }

    @Override
    public float postMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        if (this.targetInThereshold(target)) {
            return damageDealt + target.getMaxHealth();
        }
        return damageDealt;
    }
    private boolean targetInThereshold(LivingEntity target) {
        return target.getHealth() <= execute(target.getMaxHealth(), (this.getMagnitude() * this.getLevel()));
    }

    private float execute(float targetMaxHealth, float executeHealth) {
        return targetMaxHealth * executeHealth;
    }
}
