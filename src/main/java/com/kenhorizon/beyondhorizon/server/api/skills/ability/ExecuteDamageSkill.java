package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

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
        double factor = EntityUtils.getMaxHealth(target, (this.getMagnitude() * this.getLevel()));
        return target.getHealth() <= factor;
    }
}
