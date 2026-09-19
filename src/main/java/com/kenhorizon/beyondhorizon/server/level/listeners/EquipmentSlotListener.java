package com.kenhorizon.beyondhorizon.server.level.listeners;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

public class EquipmentSlotListener {
    public static final List<EquipmentSlot> ALL_SLOTS = List.of(EquipmentSlot.values());
    public static final Codec<EquipmentSlot> CODEC = Codec.STRING.xmap(EquipmentSlotListener::parseSlot, EquipmentSlotListener::slotName);

    private static EquipmentSlot parseSlot(String name) {
        return switch (name) {
            case "mainhand" -> EquipmentSlot.MAINHAND;
            case "offhand" -> EquipmentSlot.OFFHAND;
            case "feet" -> EquipmentSlot.FEET;
            case "legs" -> EquipmentSlot.LEGS;
            case "chest" -> EquipmentSlot.CHEST;
            case "head" -> EquipmentSlot.HEAD;
            default -> throw new IllegalArgumentException(
                    "Unknown equipment slot: " + name
            );
        };
    }

    private static String slotName(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> "mainhand";
            case OFFHAND -> "offhand";
            case FEET -> "feet";
            case LEGS -> "legs";
            case CHEST -> "chest";
            case HEAD -> "head";
        };
    }
}
