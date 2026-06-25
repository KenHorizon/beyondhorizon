package com.kenhorizon.beyondhorizon.datagen.loot;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class PyrolligerLootTable extends EntityLootDropBuilder {

    public PyrolligerLootTable(EntityType<?> entityType) {
        super(entityType);
    }

    @Override
    public LootTable.Builder build() {
        LootTable.Builder builder = new LootTable.Builder();
        builder
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(BHItems.ASHES_OF_FLAME.get())
                            .when(dropRate(0.35F)).apply(setCount(0, 4)).apply(lootingMultiplier(0.0F, 1.0F)))
                        .add(LootItem.lootTableItem(Items.EMERALD).when(dropRate(0.45F))
                                .apply(setCount(0, 7)).apply(lootingMultiplier(0.0F, 1.0F)))
                );
        return builder;
    }
}
