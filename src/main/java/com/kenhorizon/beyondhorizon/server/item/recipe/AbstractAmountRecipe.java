package com.kenhorizon.beyondhorizon.server.item.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractAmountRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final ItemStack result;
    protected final NonNullList<Ingredient> ingredients;

    protected AbstractAmountRecipe(ResourceLocation recipeId, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = recipeId;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
//        StackedContents contents = new StackedContents();
//        List<ItemStack> inputs = new ArrayList<>();
//        int index = 0;
//
//        for (int i = 0; i < container.getContainerSize(); ++i) {
//            ItemStack stack = container.getItem(i);
//            if (!stack.isEmpty()) {
//                ++index;
//                contents.accountStack(stack);
//                inputs.add(stack);
//            }
//        }
//
//        boolean flag = index == this.ingredients.size() && RecipeMatcher.findMatches(inputs, this.ingredients) != null;

        found:
        for (Ingredient ingredient : ingredients) {
            for (int index = 0; index < container.getContainerSize(); index++) {
                ItemStack itemStack = container.getItem(index);
                if (!itemStack.isEmpty() && ingredient.test(itemStack)) {
                    continue found;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess access) {
//        extractIngredients(container, ingredients);
        return this.getResultItem(access).copy();
    }

    public static void extractIngredients(Container container, NonNullList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            for (int index = 0; index < container.getContainerSize(); index++) {
                ItemStack itemStack = container.getItem(index);
                if (!itemStack.isEmpty() && ingredient.test(itemStack)) {
                    container.removeItem(index, ((AmountIngredient) ingredient).getCount());
                    break;
                }
            }
        }
    }

    public ItemStack assemble(Container container, Level level) {
        return assemble(container, level.registryAccess());
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@Nullable RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    protected abstract int maxIngredientSize();

    public static abstract class Serializer<R extends AbstractAmountRecipe> implements RecipeSerializer<R> {
        protected abstract R newInstance(ResourceLocation recipeId, ItemStack result, NonNullList<Ingredient> ingredients);

        @Override
        public @NotNull R fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject jsonObject) {
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true, true);
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(ingredients.size(), AmountIngredient.EMPTY);
            HashSet<Item> items = new HashSet<>();
            for (int i = 0; i < ingredients.size(); ++i) {
                JsonElement jsonElement = ingredients.get(i);
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement.getAsJsonObject(), "amount");
                AmountIngredient ingredient = AmountIngredient.Serializer.INSTANCE.parse(json);
                Item item = ingredient.getItem();
                if (items.add(item)) {
                    nonNullList.set(i, ingredient);
                } else {
                    throw new IllegalArgumentException("Duplicate ingredient " + item);
                }
            }
            if (nonNullList.isEmpty()) throw new JsonParseException("No ingredients for " + recipeId);
            R recipe = newInstance(recipeId, result, nonNullList);
            if (ingredients.size() > recipe.maxIngredientSize()) throw new IndexOutOfBoundsException("The ingredient size of '" + recipeId);
            return recipe;
        }

        @Override
        public @Nullable R fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, AmountIngredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            ItemStack result = buf.readItem();
            return newInstance(recipeId, result, ingredients);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull R recipes) {
            buf.writeVarInt(recipes.ingredients.size());
            for (Ingredient ingredient : recipes.ingredients) {
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipes.result);
        }
    }
}