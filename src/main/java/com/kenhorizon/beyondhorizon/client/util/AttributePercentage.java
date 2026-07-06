package com.kenhorizon.beyondhorizon.client.util;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public class AttributePercentage {
    private static final List<Attribute> LIST = new ArrayList<>();

    public static void init() {
        AttributePercentage.register(Attributes.KNOCKBACK_RESISTANCE);
        AttributePercentage.register(Attributes.MOVEMENT_SPEED);
        AttributePercentage.register(Attributes.FLYING_SPEED);
        AttributePercentage.register(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        AttributePercentage.register(BHAttributes.SPELLVAMP.get());
        AttributePercentage.register(BHAttributes.OMNIVAMP.get());
        AttributePercentage.register(BHAttributes.PHYSICALVAMP.get());
        AttributePercentage.register(BHAttributes.MINING_SPEED.get());
        AttributePercentage.register(BHAttributes.MINING_EFFICIENCY.get());
        AttributePercentage.register(BHAttributes.WATER_MINING_EFFICIENCY.get());
        AttributePercentage.register(BHAttributes.PERCENTAGE_MAGIC_PENETRATION.get());
        AttributePercentage.register(BHAttributes.ARMOR_PENETRATION.get());
        AttributePercentage.register(BHAttributes.MOVEMENT_EFFICIENCY.get());
        AttributePercentage.register(BHAttributes.STEALTH.get());
        AttributePercentage.register(BHAttributes.DAMAGE_DEALT.get());
        AttributePercentage.register(BHAttributes.MANA_COST.get());
        AttributePercentage.register(BHAttributes.COOLDOWN.get());
        AttributePercentage.register(BHAttributes.CAST_TIME.get());
        AttributePercentage.register(BHAttributes.HEALING.get());
        AttributePercentage.register(BHAttributes.SHIELDING.get());
        AttributePercentage.register(BHAttributes.CRITICAL_CHANCE.get());
        AttributePercentage.register(BHAttributes.CRITICAL_DAMAGE.get());
        AttributePercentage.register(BHAttributes.DAMAGE_TAKEN.get());
    }

    public static void register(Attribute attribute) {
        LIST.add(attribute);
    }

    public static Attribute get(Attribute attribute) {
        for (int i = 0; i < size(); i++) {
            if (attribute == LIST.get(i)) {
                return LIST.get(i);
            }
        }
        return null;
    }

    public static boolean isMatch(Attribute attribute) {
        return attribute == get(attribute);
    }

    public static int size() {
        return LIST.size();
    }
}
