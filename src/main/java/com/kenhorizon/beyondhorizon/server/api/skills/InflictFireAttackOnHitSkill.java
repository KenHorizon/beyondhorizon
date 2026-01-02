package com.kenhorizon.beyondhorizon.server.api.skills;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InflictFireAttackOnHitSkill extends WeaponSkills {
    public InflictFireAttackOnHitSkill(int magnitude) {
        this.setMagnitude(magnitude);
    }
    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        target.setSecondsOnFire((int) this.getMagnitude());
    }
}

