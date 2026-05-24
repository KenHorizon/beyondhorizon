package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SpellBladeAccessory extends BaseSpellbladeAccessory {

    public SpellBladeAccessory(int attackInterval, float attackScale) {
        super(attackInterval, attackScale, DamageType.PHYSICAL);
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
