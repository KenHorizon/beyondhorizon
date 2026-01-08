package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.util.Mth;

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

    public static float applyDamage(float damage, float resistance, float toughnessAttribute) {
        float toughness = BASE_ARMOR_TOUGHNESS + toughnessAttribute / NUM_ARMOR_ITEMS;
        float finalDamage = resistance - damage / toughness;
        float reduceDamage = 1.0F / (1.0F + (resistance / 100.0F));
        return damage * reduceDamage;
    }

    public static float getDamageAfterMagicAbsorb(float damage, float enchantModifiers) {
        float f = Mth.clamp(enchantModifiers, 0.0F, MAX_ARMOR);
        return damage * (1.0F - f / ARMOR_PROTECTION_DIVIDER);
    }
}
