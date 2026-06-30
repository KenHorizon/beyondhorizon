package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraft.world.entity.LivingEntity;

public record AccessorySlotContext(String identifier, LivingEntity wearer, int index) {
}
