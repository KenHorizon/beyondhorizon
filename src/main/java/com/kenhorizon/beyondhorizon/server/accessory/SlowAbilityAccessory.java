package com.kenhorizon.beyondhorizon.server.accessory;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SlowAbilityAccessory extends AccessorySkill {
    public enum Type {
        ONHIT,
        ABILITY;
    }

    private Type type = Type.ONHIT;
    public SlowAbilityAccessory(float slow, Type type) {
        this.type = type;
        this.setMagnitude(slow);
    }
}
