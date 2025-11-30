package com.kenhorizon.beyondhorizon.server.skills.skill;

import com.kenhorizon.beyondhorizon.server.StatModifiers;
import com.kenhorizon.beyondhorizon.server.skills.Skills;
import com.kenhorizon.beyondhorizon.server.skills.WeaponSkills;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class MaxHealthDamage extends WeaponSkills {
    private final float maxHealth;
    public MaxHealthDamage(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        if (this == Skills.RUINED_BLADE.get()) {
            StatModifiers modifiers = new StatModifiers(0.0F, 1.0F, 0.0F, this.maxHealth);
            float extraDamage = modifiers.applyTo(target.getMaxHealth());
            return damageDealt + extraDamage;
        }
        return damageDealt;
    }
}

