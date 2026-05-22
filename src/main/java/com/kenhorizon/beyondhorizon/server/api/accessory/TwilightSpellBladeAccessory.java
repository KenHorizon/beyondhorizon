package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class TwilightSpellBladeAccessory extends BaseSpellbladeAccessory {

    public TwilightSpellBladeAccessory(int attackInterval, float attackScale) {
        super(attackInterval, attackScale);
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
