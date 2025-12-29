package com.kenhorizon.beyondhorizon.server.effect;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TormentEffect extends BHMobEffect {
    public TormentEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(BHDamageTypes.bleed(), entity.getMaxHealth() * (Constant.TORMENT_EFFECT * amplifier));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int tick = 20 >> amplifier;
        return duration % tick == 0;
    }
}
