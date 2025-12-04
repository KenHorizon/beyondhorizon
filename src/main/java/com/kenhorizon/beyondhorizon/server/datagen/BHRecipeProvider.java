package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

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
    }
}
