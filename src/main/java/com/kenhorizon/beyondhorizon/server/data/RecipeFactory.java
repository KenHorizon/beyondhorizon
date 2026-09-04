package com.kenhorizon.beyondhorizon.server.data;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class RecipeFactory {
    protected Consumer<FinishedRecipe> consumer;
    public RecipeFactory(Consumer<FinishedRecipe> consumer) {
        this.consumer = consumer;
    }

    public Consumer<FinishedRecipe> getConsumer() {
        return consumer;
    }

    public void createSword(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        this.buildSword(baseIngredients, handle, output).save(this.consumer);
    }

    public void createSword(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        this.buildSword(baseIngredients, handle, output).save(this.consumer);
    }

    public void createAxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        this.buildAxe(baseIngredients, handle, output).save(this.consumer);
    }

    public void createAxe(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        this.buildAxe(baseIngredients, handle, output).save(this.consumer);
    }

    public void createPickaxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        this.buildPickaxe(baseIngredients, handle, output).save(this.consumer);
    }

    public void createPickaxe(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        this.buildPickaxe(baseIngredients, handle, output).save(this.consumer);
    }

    public void createShovel(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        this.buildShovel(baseIngredients, handle, output).save(this.consumer);
    }

    public void createShovel(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        this.buildShovel(baseIngredients, handle, output).save(this.consumer);
    }

    public void createHoe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        this.buildHoe(baseIngredients, handle, output).save(this.consumer);
    }

    public void createHoe(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        this.buildHoe(baseIngredients, handle, output).save(this.consumer);
    }

    public void createSpear(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        this.buildSpear(baseIngredients, handle, output).save(this.consumer);
    }

    public void createSpear(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        this.buildSpear(baseIngredients, handle, output).save(this.consumer);
    }

    public void createBlock(ItemLike baseIngredients, ItemLike output) {
        this.buildBlockOf(baseIngredients, output).save(this.consumer);
    }

    public ShapedRecipeBuilder buildBlockOf(ItemLike baseIngredients, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildSpear(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("  #")
                .pattern(" H ")
                .pattern("A  ")
                .define('#', baseIngredients)
                .define('H', handle)
                .define('A', BHItems.HANDLE.get())
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildSpear(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("  #")
                .pattern(" H ")
                .pattern("A  ")
                .define('#', baseIngredients)
                .define('H', handle)
                .define('A', BHItems.HANDLE.get())
                .unlockedBy(String.format("has_materials_for_%s", this.getName(output)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildSword(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_materials_for_%s", this.getName(output)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }


    public ShapedRecipeBuilder buildSword(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildAxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_materials_for_%s", this.getName(output)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildAxe(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildPickaxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_materials_for_%s", this.getName(output)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildPickaxe(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildHoe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_materials_for_%s", this.getName(output)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildHoe(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildShovel(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_materials_for_%s", this.getName(output)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildShovel(ItemLike baseIngredients, ItemLike handle, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildChiselled(ItemLike baseIngredients, ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("#")
                .pattern("#")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildStairs(ItemLike baseIngredients, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }


    public ShapedRecipeBuilder buildPillar(ItemLike baseIngredients, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#")
                .pattern("#")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildFence(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#|#")
                .pattern("#|#")
                .define('#', baseIngredients)
                .define('|', postMaterials)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public ShapedRecipeBuilder buildFenceGate(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("|#|")
                .pattern("|#|")
                .define('#', baseIngredients)
                .define('|', postMaterials)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildWall(ItemLike baseIngredients, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("###")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder buildGrid(ItemLike baseIngredients, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("##")
                .pattern("##")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public ShapedRecipeBuilder createSlab(ItemLike baseIngredients, ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }


    public void createStairs(ItemLike baseIngredients, ItemLike output) {
        this.buildStairs(baseIngredients, output, 8).save(this.consumer);
    }

    public void createFenceGate(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output) {
        this.buildFenceGate(baseIngredients, postMaterials, output, 4).save(this.consumer);
    }

    public void createFence(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output) {
        this.buildFence(baseIngredients, postMaterials, output, 3).save(this.consumer);
    }

    public void createPillar(ItemLike baseIngredients, ItemLike output) {
        this.buildPillar(baseIngredients, output, 2).save(this.consumer);
    }

    public void createWall(ItemLike baseIngredients, ItemLike output) {
        this.buildWall(baseIngredients, output, 4).save(this.consumer);
    }

    public void createGrid(ItemLike baseIngredients, ItemLike output) {
        this.buildGrid(baseIngredients, output, 4).save(this.consumer);
    }
    public void createGrid(ItemLike baseIngredients, ItemLike output, int count) {
        this.buildGrid(baseIngredients, output, count).save(this.consumer);
    }
    public void createSlab(ItemLike baseIngredients, ItemLike output) {
        createSlab(baseIngredients, output, 6).save(this.consumer);
    }

    public void createBasicArmor(ItemLike baseIngredients, RecipeArmorType category, ItemLike output) {
        switch (category) {
            case HELMET -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("###")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
            case CHESTPLATE -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .define('#', baseIngredients)
                        .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
            case LEGGINGS -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
            default -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(String.format("has_%s", this.getName(baseIngredients)), inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build())).save(this.consumer);
            }
        }
    }
    
    private String getName(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
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
