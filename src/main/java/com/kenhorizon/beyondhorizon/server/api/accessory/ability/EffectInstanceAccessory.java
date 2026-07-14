package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectInstanceAccessory extends AccessoryPassiveSkill {
    private MobEffectCategory category = MobEffectCategory.HARMFUL;
    public EffectInstanceAccessory(float percentageReduce, MobEffectCategory category) {
        super(percentageReduce);
        this.category = category;
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        String effectCat = Utils.formattedWords(this.category.name());
        return Component.translatable(this.createId(), effectCat, Maths.format0(this.getMagnitude()));
    }

    @Override
    public MobEffectInstance onMobEffectApplied(LivingEntity entity, MobEffectInstance instance) {
        MobEffect effect = instance.getEffect();
        if (effect.getCategory() == this.category) {
            int originalDuration = instance.getDuration();
            int newDuration = (int) (originalDuration * (1.0F - this.getMagnitude()));
            return new MobEffectInstance(effect, newDuration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon());
        } else {
            return instance;
        }
    }
}
