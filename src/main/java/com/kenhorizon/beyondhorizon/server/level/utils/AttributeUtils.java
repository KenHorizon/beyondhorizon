package com.kenhorizon.beyondhorizon.server.level.utils;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

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
    public static double getValue(LivingEntity entity, Attribute attribute) {
        return entity.getAttributeValue(attribute);
    }

    public static double getCriticalModifier(LivingEntity entity) {
        return AttributeUtils.getBonus(entity, BHAttributes.CRITICAL_DAMAGE.get());
    }
}
