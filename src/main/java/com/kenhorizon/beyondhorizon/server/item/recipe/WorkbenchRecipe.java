package com.kenhorizon.beyondhorizon.server.item.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHRecipe;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchRecipe extends AbstractAmountRecipe {
    public WorkbenchRecipe(ResourceLocation recipeId, ItemStack result, NonNullList<Ingredient> ingredient) {
        super(recipeId, result, ingredient);
    }

    @Override
    protected int maxIngredientSize() {
        return WorkbenchMenu.CRAFTING_SIZE;
    }

    @Override
    public String getGroup() {
        return "workbench";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BHBlocks.WORKBENCH.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BHRecipe.WORKBENCH_RECIPES.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BHRecipe.WORKBENCH_RECIPE_TYPES.get();
    }


    public static class Type implements RecipeType<WorkbenchRecipe> {
        public static final String ID = "workbench";
        @Override
        public String toString() {
            return BeyondHorizon.resource(ID).toString();
        }
    }

    public static class Serializer extends AbstractAmountRecipe.Serializer<WorkbenchRecipe> {

        @Override
        protected WorkbenchRecipe newInstance(ResourceLocation recipeId, ItemStack result, NonNullList<Ingredient> ingredients) {
            return new WorkbenchRecipe(recipeId, result, ingredients);
        }
    }

    public record Recipes(AmountIngredient ingredient) {}
}

