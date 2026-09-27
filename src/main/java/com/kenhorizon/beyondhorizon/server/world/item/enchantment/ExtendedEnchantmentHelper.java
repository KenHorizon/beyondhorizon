package com.kenhorizon.beyondhorizon.server.world.item.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.UUID;

public class ExtendedEnchantmentHelper {

    public static UUID getSlotUuid(EnchantmentSlotContext context) {
        String key = context.identifier() + context.index();
        return AdvancedEnchantment.UUIDS.computeIfAbsent(key, (k) -> UUID.nameUUIDFromBytes(k.getBytes()));
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack, int level) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        var stackEnchantment = EnchantmentHelper.getEnchantments(stack);
        for (var enchants : stackEnchantment.entrySet()) {
            if (enchants.getKey() instanceof IAttributeEnchantment attributeEnchantment) {
                multimap = attributeEnchantment.getAttributeModifiers(uuid, stack, level);
            }
        }
        return multimap;
    }

}
