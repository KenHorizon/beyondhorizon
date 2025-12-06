package com.kenhorizon.beyondhorizon.server.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchRecipe implements Recipe<Container> {
    protected final List<Ingredient> ingredient;
    protected final ItemStack result;
    protected final ResourceLocation recipeId;
    protected final List<Integer> counts;
    public WorkbenchRecipe(ResourceLocation recipeId, List<Ingredient> ingredient, ItemStack result, List<Integer> counts) {
        this.recipeId = recipeId;
        this.result = result;
        this.ingredient = ingredient;
        this.counts = counts;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    public ItemStack getResultItem(RegistryAccess access) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.addAll(this.ingredient);
        return nonnulllist;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        return this.result.copy();
    }

    public List<Integer> getCounts() {
        return counts;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BHBlocks.WORKBENCH.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WorkbenchRecipe.Serializer.getInstance();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.getInstance();
    }
    public static class Type implements RecipeType<WorkbenchRecipe> {
        private Type() {}
        private static final WorkbenchRecipe.Type INSTANCE = new WorkbenchRecipe.Type();
        public static final String ID = "workbench";
        public static Type getInstance() {
            return INSTANCE;
        }
    }
    public static class Serializer implements RecipeSerializer<WorkbenchRecipe> {

        private static final WorkbenchRecipe.Serializer INSTANCE = new WorkbenchRecipe.Serializer();
        public static final ResourceLocation ID = BeyondHorizon.resource("workbench");

        public static Serializer getInstance() {
            return INSTANCE;
        }

        @Override
        public WorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            JsonArray jsonIngredient = serializedRecipe.getAsJsonArray("ingredients");
            JsonArray jsonCounts = serializedRecipe.getAsJsonArray("counts");

            List<Ingredient> ingredients = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();
            for (JsonElement jsonElement : jsonIngredient) {
                ingredients.add(Ingredient.fromJson(jsonElement));
            }
            for (JsonElement jsonElement : jsonCounts) {
                counts.add(jsonElement.getAsInt());
            }
            ItemStack output = ShapedRecipe.itemStackFromJson(serializedRecipe.getAsJsonObject("result"));

            return new WorkbenchRecipe(recipeId, ingredients, output, counts);
        }

        @Override
        public @Nullable WorkbenchRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
            int size = buf.readInt();
            List<Ingredient> ingredients = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                ingredients.add(Ingredient.fromNetwork(buf));
            }

            for (int i = 0; i < size; i++) {
                counts.add(buf.readInt());
            }

            ItemStack output = buf.readItem();

            return new WorkbenchRecipe(recipeId, ingredients, output, counts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, WorkbenchRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            recipe.getIngredients().forEach(ing -> ing.toNetwork(buf));
            recipe.getCounts().forEach(buf::writeInt);
            buf.writeItem(recipe.getResultItem(null));
        }
    }

    public record Recipes(Ingredient ingredient, int count) {}
}

