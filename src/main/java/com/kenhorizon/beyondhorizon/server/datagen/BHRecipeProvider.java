package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.server.datagen.recipes.WorkbenchRecipeProvider;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class BHRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BHRecipeProvider(PackOutput output) {
        super(output);
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
