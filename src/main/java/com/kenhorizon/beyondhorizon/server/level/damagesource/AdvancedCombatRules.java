package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AdvancedCombatRules {
    public static final float MAX_ARMOR = 20.0F;
    public static final float ARMOR_PROTECTION_DIVIDER = 25.0F;
    public static final float BASE_ARMOR_TOUGHNESS = 2.0F;
    public static final float MIN_ARMOR_RATIO = 0.2F;
    private static final int NUM_ARMOR_ITEMS = 4;

    public static float getDamageAfterAbsorb(float damage, float resistance, float toughnessAttribute) {
        float f = BASE_ARMOR_TOUGHNESS + toughnessAttribute / NUM_ARMOR_ITEMS;
        float f1 = Mth.clamp(resistance - damage / f, resistance * MIN_ARMOR_RATIO, MAX_ARMOR);
        return damage * (1.0F - f1 / ARMOR_PROTECTION_DIVIDER);
    }

    public static float getDamageAfterMagicAbsorb(float damage, float enchantModifiers) {
        float f = Mth.clamp(enchantModifiers, 0.0F, MAX_ARMOR);
        return damage * (1.0F - f / ARMOR_PROTECTION_DIVIDER);
    }
}
