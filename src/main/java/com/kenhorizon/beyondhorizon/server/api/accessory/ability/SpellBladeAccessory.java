package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SpellBladeAccessory extends BaseSpellbladeAccessory {

    public SpellBladeAccessory(int attackInterval, float attackScale) {
        super(attackInterval, attackScale, DamageInfoTypes.PHYSICAL_DAMAGE);
    }

    @Override
    protected float spellBladeDamage(LivingEntity attacker, float damage, float damageScale) {
        return (float) (attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) * damageScale);
    }

    @Override
    protected String spellBladeTag() {
        return "normal_spellblade";
    }
}
