package com.kenhorizon.beyondhorizon.server.skills;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InflictFireAttackOnHitSkill extends WeaponSkills {
    private final int magnitude;
    public InflictFireAttackOnHitSkill(int magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        target.setSecondsOnFire(this.magnitude);
    }
}

