package com.kenhorizon.beyondhorizon.server.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.UUID;

public class ExtendedEnchantmentHelper {

    public static UUID getSlotUuid(EnchantmentSlotContext context) {
        String key = context.identifier() + context.index();
        return AdvancedEnchantment.UUIDS.computeIfAbsent(key, (k) -> UUID.nameUUIDFromBytes(k.getBytes()));
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        CompoundTag nbt = stack.getOrCreateTag();
        if ((!stack.isEmpty() && !nbt.isEmpty()) && nbt.contains(AdvancedEnchantment.ENCHANTMENT_TAGS, 9)) {
            ListTag nbtList = nbt.getList(AdvancedEnchantment.ENCHANTMENT_TAGS, 10);
            for (int i = 0; i < nbtList.size(); ++i) {
                CompoundTag tags = nbtList.getCompound(i);
                Optional<Attribute> optional = Optional.ofNullable(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(tags.getString("enchantments_name"))));
                if (optional.isPresent()) {
                    AttributeModifier attributeModifier = AttributeModifier.load(tags);
                    if (attributeModifier != null && attributeModifier.getId().getLeastSignificantBits() != 0L && attributeModifier.getId().getMostSignificantBits() != 0L) {
                        multimap.put(optional.get(), attributeModifier);
                    }
                }
            }
        } else {
            var stackEnchantment = EnchantmentHelper.getEnchantments(stack);
            for (var enchants : stackEnchantment.entrySet()) {
                if (enchants.getKey() instanceof IAttributeEnchantment attributeEnchantment) {
                    multimap = attributeEnchantment.getAttributeModifiers(uuid, stack);
                }
            }
        }
        return multimap;
    }

}
