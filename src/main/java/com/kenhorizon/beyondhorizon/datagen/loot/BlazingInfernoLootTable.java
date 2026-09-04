package com.kenhorizon.beyondhorizon.datagen.loot;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class BlazingInfernoLootTable extends EntityLootDropBuilder {

    public BlazingInfernoLootTable(EntityType<?> entityType) {
        super(entityType);
    }

    @Override
    public LootTable.Builder build() {
        LootTable.Builder builder = new LootTable.Builder();
        builder
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BLAZE_ROD).apply(setCount(12, 16)).apply(lootingMultiplier(0.0F, 1.0F)))
                        .add(LootItem.lootTableItem(BHItems.WILDFIRE_FRAGMENT.get()).apply(setCount(15, 20)).apply(lootingMultiplier(0.0F, 1.0F)))
                )
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(BHItems.GUARDIAN_SWORD.get()).when(randomChanceAndLootingBoost(0.35F, 1.25F)))
                        .add(LootItem.lootTableItem(BHItems.HEART_OF_THE_INFERNO.get()).when(randomChanceAndLootingBoost(0.15F, 1.25F)))
                );
        return builder;
    }
}
