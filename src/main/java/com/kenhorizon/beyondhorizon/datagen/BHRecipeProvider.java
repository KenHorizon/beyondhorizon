package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.data.RecipeFacory;
import com.kenhorizon.beyondhorizon.datagen.recipes.WorkbenchRecipeProvider;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class BHRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BHRecipeProvider(PackOutput output) {
        super(output);
    }

    private void woolFurToWoolBlock(RecipeFacory recipeFactory, ItemLike builder, ItemLike output, int count) {
        recipeFactory.buildGrid(builder, output, count, "has_white_wool_fur").save(recipeFactory.getConsumer(), this.getConversionRecipeNameTwoByTwo(output, builder));
    }
    private void woolFurToWoolBlock(RecipeFacory recipeFactory, ItemLike builder, ItemLike output) {
        this.woolFurToWoolBlock(recipeFactory, builder, output, 1);
    }

    protected String getConversionRecipeNameTwoByTwo(ItemLike result, ItemLike ingredient) {
        return String.format("%s_from_%s", getItemName(result), getItemName(ingredient));
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        RecipeFacory recipeFactory = new RecipeFacory(consumer);
        recipeFactory.createGrid(Items.IRON_NUGGET, BHItems.CHAIN_PLATE.get(), 1, "has_iron_nuggets");

        this.woolFurToWoolBlock(recipeFactory, BHItems.WHITE_WOOL_FUR.get(), Blocks.WHITE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.ORANGE_WOOL_FUR.get(), Blocks.ORANGE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.MAGENTA_WOOL_FUR.get(), Blocks.MAGENTA_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.LIGHT_BLUE_WOOL_FUR.get(), Blocks.LIGHT_BLUE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.YELLOW_WOOL_FUR.get(), Blocks.YELLOW_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.LIME_WOOL_FUR.get(), Blocks.LIME_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.PINK_WOOL_FUR.get(), Blocks.PINK_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.GRAY_WOOL_FUR.get(), Blocks.GRAY_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.LIGHT_GRAY_WOOL_FUR.get(), Blocks.LIGHT_GRAY_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.CYAN_WOOL_FUR.get(), Blocks.CYAN_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.PURPLE_WOOL_FUR.get(), Blocks.PURPLE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.BLUE_WOOL_FUR.get(), Blocks.BLUE_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.BROWN_WOOL_FUR.get(), Blocks.BROWN_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.GREEN_WOOL_FUR.get(), Blocks.GREEN_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.RED_WOOL_FUR.get(), Blocks.RED_WOOL);
        this.woolFurToWoolBlock(recipeFactory, BHItems.BLACK_WOOL_FUR.get(), Blocks.BLACK_WOOL);
        //
        recipeFactory.createSword(BHItems.HELLSTONE_INGOT.get(), Items.IRON_INGOT, BHItems.HELLSTONE_SWORD.get(), "hellstone_sword");
        recipeFactory.createPickaxe(BHItems.HELLSTONE_INGOT.get(), Items.IRON_INGOT, BHItems.HELLSTONE_PICKAXE.get(), "hellstone_pickaxe");
        recipeFactory.createAxe(BHItems.HELLSTONE_INGOT.get(), Items.IRON_INGOT, BHItems.HELLSTONE_AXE.get(), "hellstone_axe");
        recipeFactory.createShovel(BHItems.HELLSTONE_INGOT.get(), Items.IRON_INGOT, BHItems.HELLSTONE_SHOVEL.get(), "hellstone_shovel");
        recipeFactory.createHoe(BHItems.HELLSTONE_INGOT.get(), Items.IRON_INGOT, BHItems.HELLSTONE_HOE.get(), "hellstone_hoe");

        recipeFactory.createSword(BHItems.STARITE_INGOT.get(), Items.IRON_INGOT, BHItems.STARITE_SWORD.get(), "starite_sword");
        recipeFactory.createPickaxe(BHItems.STARITE_INGOT.get(), Items.IRON_INGOT, BHItems.STARITE_PICKAXE.get(), "starite_pickaxe");
        recipeFactory.createAxe(BHItems.STARITE_INGOT.get(), Items.IRON_INGOT, BHItems.STARITE_AXE.get(), "starite_axe");
        recipeFactory.createShovel(BHItems.STARITE_INGOT.get(), Items.IRON_INGOT, BHItems.STARITE_SHOVEL.get(), "starite_shovel");
        recipeFactory.createHoe(BHItems.STARITE_INGOT.get(), Items.IRON_INGOT, BHItems.STARITE_HOE.get(), "starite_hoe");
        //
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICKS.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_PILLAR.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_SLAB.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_STAIRS.get(), BHBlocks.END_GREY_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_STAIRS.get(), BHBlocks.END_GREY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_GREY_BRICK_SLAB.get(), BHBlocks.END_GREY_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.END_STONE_TILES.get(), Blocks.END_STONE);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS,BHBlocks.CHISILLED_END_STONE.get(), Blocks.END_STONE);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, BHBlocks.CHISILLED_END_STONE_UNCARVED.get(), Blocks.END_STONE);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 4)
                .requires(BHItemTags.WOOL_FUR)
                .unlockedBy("has_wool_fur",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItemTags.WOOL_FUR).build()))
                .save(consumer, getItemName(Items.STRING) + "_from_wool_fur");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BHBlocks.WORKBENCH.get())
                .pattern("PCP")
                .pattern("IAI")
                .pattern("PPP")
                .define('C', Blocks.CRAFTING_TABLE)
                .define('A', Blocks.ANVIL)
                .define('P', ItemTags.PLANKS)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_crafting_table",
                        inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.CRAFTING_TABLE).build()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BHItems.RAW_EMBED_HELLSTONE.get())
                .requires(BHItems.RAW_HELLSTONE.get())
                .requires(Blocks.OBSIDIAN)
                .unlockedBy("has_raw_hellstone",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItems.RAW_HELLSTONE.get()).build()))
                .save(consumer);

        WorkbenchRecipeProvider.create(BHItems.NULL_MAGIC_RUNE.get(), 1)
                .required(Items.EMERALD, 20)
                .required(BHItems.RUBY.get(), 20)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.RUMINATIVE_BEADS.get(), 1)
                .required(Items.EMERALD, 20)
                .required(Items.LEAD, 6)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.TOUGH_CLOTH.get(), 1)
                .required(Items.LEATHER, 32)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.CHAIN_VEST.get(), 1)
                .required(BHItems.TOUGH_CLOTH.get())
                .required(BHItems.TOUGH_CLOTH.get())
                .required(BHItems.CHAIN_PLATE.get(), 20)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.VITALITY_STONE.get(), 1)
                .required(BHItems.RUBY.get(), 20)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.BOOTS.get(), 1)
                .required(Items.LEATHER, 5)
                .required(Items.IRON_NUGGET, 3)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.IRON_PLATED_BOOTS.get(), 1)
                .required(BHItems.BOOTS.get())
                .required(BHItems.CHAIN_PLATE.get(), 3)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.BERSERKER_BOOTS.get(), 1)
                .required(BHItems.BOOTS.get())
                .required(BHItems.SWIFT_DAGGER.get())
                .required(BHItems.SWIFT_DAGGER.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.AETHER_WISP.get(), 1)
                .required(BHItems.AMPLIFLYING_TOME.get(), 3)
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.SPECTRAL_CLOAK.get(), 1)
                .required(BHItems.RUMINATIVE_BEADS.get())
                .required(BHItems.NULL_MAGIC_RUNE.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.CINDER_STONE.get(), 1)
                .required(BHItems.VITALITY_STONE.get())
                .required(BHItems.VITALITY_STONE.get())
                .required(BHItems.FIREFLY_FAYE.get())
                .save(consumer);
        WorkbenchRecipeProvider.create(BHItems.UNSTABLE_RUNIC_TOME.get(), 1)
                .required(BHItems.AMPLIFLYING_TOME.get())
                .required(BHItems.FIREFLY_FAYE.get())
                .required(BHItems.SAPPHIRE_CRYSTAL.get())
                .save(consumer);

        oreSmeltings(consumer, List.of(BHItems.RAW_EMBED_HELLSTONE.get()), RecipeCategory.MISC, BHItems.HELLSTONE_INGOT.get(), 0.7F, Maths.sec(10));
        oreSmeltings(consumer, List.of(BHItems.RAW_STARITE.get()), RecipeCategory.MISC, BHItems.STARITE_INGOT.get(), 0.7F, Maths.sec(10));

    }

    protected static void oreSmeltings(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime) {
        smelting(consumer, ingredients, category, result, exp, cookingTime, "_from_smelting");
        blasting(consumer, ingredients, category, result, exp, cookingTime / 2, "_from_blasting");

    }

    protected static void woodSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime) {
        smelting(consumer, ingredients, category, result, exp, cookingTime, "_from_smelting");
        smoker(consumer, ingredients, category, result, exp, cookingTime / 2, "_from_smoking");

    }

    protected static void smelting(Consumer<FinishedRecipe> consumer, String id, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.SMELTING_RECIPE, ingredients, category, result, exp, cookingTime, group,  "_" + id + "_from_smelting");
    }

    protected static void smelting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.SMELTING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_smelting");
    }

    protected static void blasting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.BLASTING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_blasting");
    }

    protected static void smoker(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.SMOKING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_smoking");
    }

    protected static void campfire(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group) {
        cooking(consumer, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, ingredients, category, result, exp, cookingTime, group, "_from_campfire");
    }

    protected static void cooking(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends AbstractCookingRecipe> serializer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float exp, int cookingTime, String group, String recipeName) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, exp, cookingTime, serializer).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(consumer, BeyondHorizon.ID + ":" + getItemName(result) + recipeName + "_" + getItemName(itemlike));
        }
    }
}
