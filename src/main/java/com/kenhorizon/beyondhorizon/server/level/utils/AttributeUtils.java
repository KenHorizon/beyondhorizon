package com.kenhorizon.beyondhorizon.server.level.utils;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.stream.Collectors;

public class AttributeUtils {
    public static double getBonus(LivingEntity entity, Attribute attribute) {
        AttributeMap attributeMap = entity.getAttributes();
        double amount = 0;
        if (attributeMap.hasAttribute(attribute)) {
            AttributeInstance instance = entity.getAttribute(attribute);
            if (instance != null) {
                for (var sets : instance.getModifiers()) {
                    if (sets.getOperation() == AttributeModifier.Operation.ADDITION) {
                        amount += (entity.getAttributeValue(attribute) - sets.getAmount());
                    }
                }
            }
        }
        BeyondHorizon.LOGGER.info("Amount: {}", amount);
        return amount;
    }

    public static double getValue(LivingEntity entity, Attribute attribute) {
        return entity.getAttributeValue(attribute);
    }

    public static double getCriticalModifier(LivingEntity entity) {
        return AttributeUtils.getBonus(entity, BHAttributes.CRITICAL_DAMAGE.get());
    }
}
