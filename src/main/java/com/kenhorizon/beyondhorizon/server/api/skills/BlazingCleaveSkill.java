package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BlazingCleaveSkill extends CleaveEffectSkill {
    public BlazingCleaveSkill(float magnitude, float range) {
        super(magnitude, range, CleaveAbility.Type.CIRCLE);
    }

    @Override
    public boolean coneAtTarget() {
        return true;
    }

    @Override
    public void attackCleave(ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        CleaveAbility.spawn(attacker.level(), target , attacker, this.dealDamage(target, attacker, damageDealt, itemStack), this.getCleaveRange());

    }

    @Override
    public float dealDamage(LivingEntity target, LivingEntity attacker, float damageDealt, ItemStack itemStack) {
        return damageDealt * this.getMagnitude();
    }
}
