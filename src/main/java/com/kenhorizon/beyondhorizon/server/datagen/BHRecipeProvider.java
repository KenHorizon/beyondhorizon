package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.server.data.RecipeGenProviderHelper;
import com.kenhorizon.beyondhorizon.server.datagen.recipes.WorkbenchRecipeProvider;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class BHRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BHRecipeProvider(PackOutput output) {
        super(output);
    }

    private void woolFurToWoolBlock(Consumer<FinishedRecipe> consumer, ItemLike builder, ItemLike output, int count) {
        RecipeGenProviderHelper.createGrid(builder, output, count, "has_white_wool_fur").save(consumer, this.getConversionRecipeNameTwoByTwo(output, builder));
    }
    private void woolFurToWoolBlock(Consumer<FinishedRecipe> consumer, ItemLike builder, ItemLike output) {
        this.woolFurToWoolBlock(consumer, builder, output, 1);
    }

    protected String getConversionRecipeNameTwoByTwo(ItemLike result, ItemLike ingredient) {
        return String.format("%s_from_%s", getItemName(result), getItemName(ingredient));
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BHItems.CHAIN_PLATE.get())
                .pattern("##")
                .pattern("##")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy("has_iron_nugget",
                        inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_NUGGET).build()))
                .save(consumer);

        this.woolFurToWoolBlock(consumer, BHItems.WHITE_WOOL_FUR.get(), Blocks.WHITE_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.ORANGE_WOOL_FUR.get(), Blocks.ORANGE_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.MAGENTA_WOOL_FUR.get(), Blocks.MAGENTA_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.LIGHT_BLUE_WOOL_FUR.get(), Blocks.LIGHT_BLUE_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.YELLOW_WOOL_FUR.get(), Blocks.YELLOW_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.LIME_WOOL_FUR.get(), Blocks.LIME_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.PINK_WOOL_FUR.get(), Blocks.PINK_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.GRAY_WOOL_FUR.get(), Blocks.GRAY_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.LIGHT_GRAY_WOOL_FUR.get(), Blocks.LIGHT_GRAY_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.CYAN_WOOL_FUR.get(), Blocks.CYAN_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.PURPLE_WOOL_FUR.get(), Blocks.PURPLE_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.BLUE_WOOL_FUR.get(), Blocks.BLUE_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.BROWN_WOOL_FUR.get(), Blocks.BROWN_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.GREEN_WOOL_FUR.get(), Blocks.GREEN_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.RED_WOOL_FUR.get(), Blocks.RED_WOOL);
        this.woolFurToWoolBlock(consumer, BHItems.BLACK_WOOL_FUR.get(), Blocks.BLACK_WOOL);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 4)
                .requires(BHItemTags.WOOL_FUR)
                .unlockedBy("has_wool_fur",
                        inventoryTrigger(ItemPredicate.Builder.item().of(BHItemTags.WOOL_FUR).build()))
                .save(consumer);

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
    }
}
