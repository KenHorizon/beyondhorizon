package com.kenhorizon.beyondhorizon.client.util;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public class InvertedAttributeColorFormat {
    private static final List<Attribute> LIST = new ArrayList<>();

    public static void init() {
        InvertedAttributeColorFormat.register(BHAttributes.MANA_COST.get());
        InvertedAttributeColorFormat.register(BHAttributes.CAST_TIME.get());
        InvertedAttributeColorFormat.register(BHAttributes.DAMAGE_TAKEN.get());
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
