package com.kenhorizon.beyondhorizon.server.level.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AttributeUtils {
    public static double getBonus(LivingEntity entity, Attribute attribute) {
        var instance = entity.getAttribute(attribute);
        var attrMaps = entity.getAttributes();
        if (instance != null && instance.getModifiers().isEmpty()) {
            return entity.getAttributeValue(attribute) - entity.getAttributeBaseValue(attribute);
        } else {
            return entity.getAttributeBaseValue(attribute) - entity.getAttributeValue(attribute);
        }
    }
    public static double getTotal(LivingEntity entity, Attribute attribute) {
        return entity.getAttributeValue(attribute);
    }
}
