package com.kenhorizon.beyondhorizon.server.data;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeFacory {
    protected Consumer<FinishedRecipe> consumer;
    public RecipeFacory(Consumer<FinishedRecipe> consumer) {
        this.consumer = consumer;
    }

    public Consumer<FinishedRecipe> getConsumer() {
        return consumer;
    }

    public void createSword(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildSword(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createSword(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildSword(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createAxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildAxe(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createAxe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildAxe(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createPickaxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildPickaxe(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createPickaxe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildPickaxe(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createShovel(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildShovel(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createShovel(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildShovel(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createHoe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildHoe(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public void createHoe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        this.buildHoe(baseIngredients, handle, output, criterionName).save(this.consumer);
    }

    public ShapedRecipeBuilder buildSword(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }


    public ShapedRecipeBuilder buildSword(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildAxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildAxe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildPickaxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildPickaxe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildHoe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildHoe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildShovel(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildShovel(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildChiselled(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("#")
                .pattern("#")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildStairs(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }


    public ShapedRecipeBuilder buildPillar(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#")
                .pattern("#")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildFence(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#|#")
                .pattern("#|#")
                .define('#', baseIngredients)
                .define('|', postMaterials)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildFenceGate(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("|#|")
                .pattern("|#|")
                .define('#', baseIngredients)
                .define('|', postMaterials)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildWall(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("###")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildGrid(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("##")
                .pattern("##")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder createSlab(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }


    public void createStairs(ItemLike baseIngredients, ItemLike output, String criterionName) {
        this.buildStairs(baseIngredients, output, 8, criterionName).save(this.consumer);
    }

    public void createFenceGate(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, String criterionName) {
        this.buildFenceGate(baseIngredients, postMaterials, output, 4, criterionName).save(this.consumer);
    }

    public void createFence(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, String criterionName) {
        this.buildFence(baseIngredients, postMaterials, output, 3, criterionName).save(this.consumer);
    }

    public void createPillar(ItemLike baseIngredients, ItemLike output, String criterionName) {
        this.buildPillar(baseIngredients, output, 2, criterionName).save(this.consumer);
    }

    public void createWall(ItemLike baseIngredients, ItemLike output, String criterionName) {
        this.buildWall(baseIngredients, output, 4, criterionName).save(this.consumer);
    }

    public void createGrid(ItemLike baseIngredients, ItemLike output, String criterionName) {
        this.buildGrid(baseIngredients, output, 4, criterionName).save(this.consumer);
    }
    public void createGrid(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        this.buildGrid(baseIngredients, output, count, criterionName).save(this.consumer);
    }
    public void createSlab(ItemLike baseIngredients, ItemLike output, String criterionName) {
        createSlab(baseIngredients, output, 6, criterionName).save(this.consumer);
    }

    public void createBasicArmor(ItemLike baseIngredients, RecipeArmorType category, ItemLike output, String criterionName) {
        switch (category) {
            case HELMET -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("###")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
            case CHESTPLATE -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
            case LEGGINGS -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
            default -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
        }
    }

    public enum RecipeArmorType {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

    private InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pPredicates);
    }
}
