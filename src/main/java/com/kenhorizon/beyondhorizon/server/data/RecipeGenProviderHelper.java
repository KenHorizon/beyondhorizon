package com.kenhorizon.beyondhorizon.server.data;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

public class RecipeGenProviderHelper {
    public static ShapedRecipeBuilder createBattleAxe(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#H#")
                .pattern("#S#")
                .pattern(" S ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .define('S', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createBattleAxe(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#H#")
                .pattern("#S#")
                .pattern(" S ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .define('S', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createClaymore(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("  #")
                .pattern("## ")
                .pattern("H# ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createClaymore(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("  #")
                .pattern("## ")
                .pattern("H# ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createScythe(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern("  H")
                .pattern("  S")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createScythe(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern("  H")
                .pattern("  S")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .define('S', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createKatana(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("# ")
                .pattern("H ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createKatana(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("# ")
                .pattern("H ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createDagger(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createDagger(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSickle(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("## ")
                .pattern("  #")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSickle(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("## ")
                .pattern("  #")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createCrossbow(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("H#H")
                .pattern("STS")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('S', Items.STRING)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createCrossbow(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("H#H")
                .pattern("STS")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('S', Items.STRING)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHeavyCrossbow(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#L#")
                .pattern("STS")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('L', ItemTags.LOGS)
                .define('S', Items.STRING)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHeavyCrossbow(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#L#")
                .pattern("STS")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('L', ItemTags.LOGS)
                .define('S', Items.STRING)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHammer(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern("###")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHammer(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern("###")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSword(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSword(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createAxe(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createAxe(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createPickaxe(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createPickaxe(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHoe(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHoe(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createShovel(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createShovel(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', Items.STICK)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createSpear(TagKey<Item> baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("  #")
                .pattern(" H ")
                .pattern("S  ")
                .define('#', baseIngredients)
                .define('S', Items.STICK)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSpear(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("  #")
                .pattern(" H ")
                .pattern("S  ")
                .define('#', baseIngredients)
                .define('S', Items.STICK)
                .define('H', BHItems.HANDLE.get())
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSword(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createSword(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createAxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createAxe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern("#H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createPickaxe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createPickaxe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("###")
                .pattern(" H ")
                .pattern(" H ")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHoe(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createHoe(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("##")
                .pattern(" H")
                .pattern(" H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createShovel(TagKey<Item> baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createShovel(ItemLike baseIngredients, ItemLike handle, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                .pattern("#")
                .pattern("H")
                .pattern("H")
                .define('#', baseIngredients)
                .define('H', handle)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createChiselled(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("#")
                .pattern("#")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createStairs(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }

    public static ShapedRecipeBuilder createStairs(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return createStairs(baseIngredients, output, 8, criterionName);
    }

    public static ShapedRecipeBuilder createPillar(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#")
                .pattern("#")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createFence(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("#|#")
                .pattern("#|#")
                .define('#', baseIngredients)
                .define('|', postMaterials)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createFenceGate(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("|#|")
                .pattern("|#|")
                .define('#', baseIngredients)
                .define('|', postMaterials)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createWall(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("###")
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createGrid(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("##")
                .pattern("##")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createFenceGate(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, String criterionName) {
        return createFenceGate(baseIngredients, postMaterials, output, 4, criterionName);
    }
    public static ShapedRecipeBuilder createFence(ItemLike baseIngredients, ItemLike postMaterials, ItemLike output, String criterionName) {
        return createFence(baseIngredients, postMaterials, output, 3, criterionName);
    }

    public static ShapedRecipeBuilder createPillar(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return createPillar(baseIngredients, output, 2, criterionName);
    }
    public static ShapedRecipeBuilder createWall(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return createWall(baseIngredients, output, 4, criterionName);
    }
    public static ShapedRecipeBuilder createGrid(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return createGrid(baseIngredients, output, 4, criterionName);
    }
    public static ShapedRecipeBuilder createSlab(ItemLike baseIngredients, ItemLike output, int count, String criterionName) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, count)
                .pattern("###")
                .define('#', baseIngredients)
                .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
    }
    public static ShapedRecipeBuilder createSlab(ItemLike baseIngredients, ItemLike output, String criterionName) {
        return createSlab(baseIngredients, output, 6, criterionName);
    }
    public static ShapedRecipeBuilder createBasicArmor(ItemLike baseIngredients, RecipeArmorType category, ItemLike output, String criterionName) {
        switch (category) {
            case HELMET -> {
                return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("###")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
            }
            case CHESTPLATE -> {
                return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("# #")
                        .pattern("###")
                        .pattern("###")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
            }
            case LEGGINGS -> {
                return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("###")
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
            }
            default -> {
                return ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output)
                        .pattern("# #")
                        .pattern("# #")
                        .define('#', baseIngredients)
                        .unlockedBy(criterionName, inventoryTrigger(ItemPredicate.Builder.item().of(baseIngredients).build()));
            }
        }
    }
    public enum RecipeArmorType {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pPredicates);
    }
}
