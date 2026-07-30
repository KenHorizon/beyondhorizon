package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class TwilightSpellBladeAccessory extends BaseSpellbladeAccessory {

    public TwilightSpellBladeAccessory(int attackInterval, float attackScale) {
        super(attackInterval, attackScale, DamageType.MAGIC_DAMAGE);
    }

    @Override
    protected float spellBladeDamage(LivingEntity attacker, float damage, float damageScale) {
        return (float) (attacker.getAttributeValue(BHAttributes.ABILITY_POWER.get()) * damageScale);
    }

    @Override
    protected String spellBladeTag() {
        return "twilight_spellblade";
    }
}
