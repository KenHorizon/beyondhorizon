package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.server.datagen.recipes.WorkbenchRecipeProvider;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
    }
}
