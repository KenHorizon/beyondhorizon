package com.kenhorizon.beyondhorizon.server.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ModifierEffect extends BHMobEffect {
    protected final double multiplier;

    public ModifierEffect(MobEffectCategory category, int color, double multiplier) {
        super(category, color);
        this.multiplier = multiplier;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return this.multiplier * (double) (amplifier + 1);
    }
}
