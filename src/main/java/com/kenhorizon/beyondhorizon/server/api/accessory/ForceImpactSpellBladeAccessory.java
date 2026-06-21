package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ForceImpactSpellBladeAccessory extends BaseSpellbladeAccessory {

    public ForceImpactSpellBladeAccessory(int attackInterval, float attackScale) {
        super(attackInterval, attackScale, DamageType.PHYSICAL);
    }

    @Override
    protected float spellBladeDamage(LivingEntity attacker, float damage, float damageScale) {
        return (float) (attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageScale);
    }

    @Override
    protected String spellBladeTag() {
        return "force_impact_spellblade";
    }
}
