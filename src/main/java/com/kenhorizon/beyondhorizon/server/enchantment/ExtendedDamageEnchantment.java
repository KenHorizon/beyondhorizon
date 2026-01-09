package com.kenhorizon.beyondhorizon.server.enchantment;

import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class ExtendedDamageEnchantment extends AdvancedEnchantment {
    public ExtendedDamageEnchantment(Builder builder) {
        super(builder);
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof ExtendedDamageEnchantment) && !(other instanceof DamageEnchantment);
    }
}
