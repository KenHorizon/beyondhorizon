package com.kenhorizon.beyondhorizon.server.level.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AttributeUtils {
    public static double getBonus(LivingEntity entity, Attribute attribute) {
        return entity.getAttributeValue(attribute) - entity.getAttributeBaseValue(attribute);
    }
}
