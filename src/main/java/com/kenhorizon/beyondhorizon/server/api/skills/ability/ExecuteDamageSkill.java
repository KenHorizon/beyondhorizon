package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ExecuteDamageSkill extends WeaponPassiveSkills {
    public ExecuteDamageSkill(float magnitude) {
        this.setMagnitude(magnitude);
    }

    @Override
    public float postMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return context.damage();
        if (this.targetInThereshold(target)) {
            return context.add(target.getMaxHealth());
        }
        return context.damage();
    }
    private boolean targetInThereshold(LivingEntity target) {
        return target.getHealth() <= execute(target.getMaxHealth(), (this.getMagnitude() * this.getLevel()));
    }

    private float execute(float targetMaxHealth, float executeHealth) {
        return targetMaxHealth * executeHealth;
    }
}
