package com.kenhorizon.beyondhorizon.server.level.entity;

import com.google.common.collect.Maps;
import com.kenhorizon.beyondhorizon.server.level.listeners.EquipmentSlotListener;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EquipmentTable {
    public static final Codec<Map<EquipmentSlot, Float>> DROP_CHANCES_CODEC = Codec.either(Codec.FLOAT, Codec.unboundedMap(EquipmentSlotListener.CODEC, Codec.FLOAT))
            .xmap(either -> either.map(EquipmentTable::createForAllSlots, Function.identity()), (provider) -> {
                boolean dropChancesTheSame = provider.values().stream().distinct().count() == 1L;
                boolean allSlotsArePresent = provider.keySet().containsAll(EquipmentSlotListener.ALL_SLOTS);
                return dropChancesTheSame && allSlotsArePresent ? Either.left((Float) provider.values().stream().findFirst().orElse(0.0F))
                        : Either.right(provider);
            });
    public static final Codec<EquipmentTable> CODEC =
            RecordCodecBuilder.create(i -> i.group(
                    ResourceLocation.CODEC.fieldOf("loot_table")
                            .forGetter(EquipmentTable::getLootTable),
                    DROP_CHANCES_CODEC.optionalFieldOf("slot_drop_chances", Map.of()).forGetter(EquipmentTable::getDropChances)
            ).apply(i, EquipmentTable::new));

    protected ResourceLocation lootTable;
    protected Map<EquipmentSlot, Float> dropChances;

    public EquipmentTable(ResourceLocation lootTable, Map<EquipmentSlot, Float> dropChances) {
        this.lootTable = lootTable;
        this.dropChances = dropChances;
    }

    public EquipmentTable(ResourceLocation lootTable, float dropChance) {
        this(lootTable, createForAllSlots(dropChance));
    }

    private static Map<EquipmentSlot, Float> createForAllSlots(float dropChance) {
        return createForAllSlots(List.of(EquipmentSlot.values()), dropChance);
    }

    private static Map<EquipmentSlot, Float> createForAllSlots(List<EquipmentSlot> slots, float dropChance) {
        Map<EquipmentSlot, Float> map = Maps.newHashMap();
        for (EquipmentSlot slot : slots) {
            map.put(slot, dropChance);
        }
        return map;
    }

    public Map<EquipmentSlot, Float> getDropChances() {
        return dropChances;
    }

    public ResourceLocation getLootTable() {
        return lootTable;
    }

    @Override
    public String toString() {
        return String.format("ResouceLocation=%s | Map=%s",this.getLootTable(), this.getDropChances());
    }
}
