package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class ArmorBonusSets {
    public static UUID DIAMOND_SET_UUID = UUID.fromString("5ff0f2f4-1ae2-4abc-9ab6-6cfd9318b0a2");

    public static void register() {
//        ArmorSetRegistry.register(new ArmorBonusSet(BeyondHorizon.resource("iron_armor_set"),
//                new ItemStack(Items.IRON_HELMET),
//                new ItemStack(Items.IRON_CHESTPLATE),
//                new ItemStack(Items.IRON_LEGGINGS),
//                new ItemStack(Items.IRON_BOOTS),
//                player -> {
//                    AttributeInstance attr = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
//                    if (attr != null && attr.getModifier(DIAMOND_SET_UUID) == null) {
//                        attr.addPermanentModifier(new AttributeModifier(DIAMOND_SET_UUID, "Set Armor Bonus", 2.0, AttributeModifier.Operation.ADDITION));
//                    }
//                    },
//                player -> {
//                    AttributeInstance attr = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
//                    if (attr != null) {
//                        attr.removeModifier(DIAMOND_SET_UUID);
//                    }
//        }
//        ));

    }
}
