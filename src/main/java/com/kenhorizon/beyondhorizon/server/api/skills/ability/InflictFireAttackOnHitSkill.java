package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InflictFireAttackOnHitSkill extends WeaponPassiveSkills {
    public InflictFireAttackOnHitSkill(int magnitude) {
        this.setMagnitude(magnitude);
    }
    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext damageDealt) {
        target.setSecondsOnFire((int) this.getMagnitude());
    }
}

