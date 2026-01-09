package com.kenhorizon.beyondhorizon.datagen.loot;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public abstract class EntityLootDropBuilder {
    protected EntityType<?> entityType;

    public EntityLootDropBuilder(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public abstract LootTable.Builder build();

    protected LootItemEntityPropertyCondition.Builder hasProperties(LootContext.EntityTarget entityTarget, EntityPredicate.Builder predicateBuilder) {
        return LootItemEntityPropertyCondition.hasProperties(entityTarget, predicateBuilder);
    }
    protected LootItemCondition.Builder dropRate(float chances) {
        return LootItemRandomChanceCondition.randomChance(chances);
    }
    protected LootItemCondition.Builder randomChanceAndLootingBoost(float chances, float mulitplier) {
        return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chances, mulitplier);
    }

    protected LootPool.Builder rolls(float rolls) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(rolls));
    }

    protected LootItemCondition.Builder killedByPlayer() {
        return LootItemKilledByPlayerCondition.killedByPlayer();
    }

    protected LootItemFunction.Builder lootingMultiplier() {
        return lootingMultiplier(0.0F, 1.0F);
    }

    protected LootItemFunction.Builder lootingMultiplier(float min, float max) {
        return LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(min, max));
    }

    protected SetItemCountFunction.Builder<?> setCount(float count) {
        return SetItemCountFunction.setCount(UniformGenerator.between(count, count));
    }

    protected SetItemCountFunction.Builder<?> setCount(float min, float max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    protected LootItem.Builder<?> item(ItemLike items) {
        return LootItem.lootTableItem(items);
    }
}
