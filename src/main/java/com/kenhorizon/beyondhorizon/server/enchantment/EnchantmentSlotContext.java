package com.kenhorizon.beyondhorizon.server.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public record EnchantmentSlotContext(String identifier, LivingEntity wearer, EquipmentSlot index) {
}
