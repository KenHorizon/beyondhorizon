package com.kenhorizon.beyondhorizon.server.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public record EnchantmentSlotContext(String identifier, LivingEntity wearer, EquipmentSlot index) {
}
