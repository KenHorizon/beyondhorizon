package com.kenhorizon.libs.server.item.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

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
        return AbstractAmountRecipe.matches(container.getContainerSize(), container::getItem, this.ingredients);
//        return matches(container, this.ingredients);
    }

    public static boolean matches(Container container, NonNullList<Ingredient> ingredients) {
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

    public static boolean matches(int size, Int2ObjectFunction<ItemStack> getItemStackCallback, NonNullList<Ingredient> ingredients) {
        HashSet<Ingredient> matches = new HashSet<>();
        Object2IntOpenHashMap<Integer> requires2Count = new Object2IntOpenHashMap<>();
        outer:
        for (int j = 0; j < ingredients.size(); j++) {
            Ingredient ingredient = ingredients.get(j);
            for (int i = 0; i < size; i++) {
                ItemStack itemStack = getItemStackCallback.apply(i);
                if (itemStack.isEmpty()) continue;
                if (ingredient instanceof AmountIngredient amountIngredient) {
                    if (amountIngredient.test(itemStack)) {
                        requires2Count.addTo(j, itemStack.getCount());
                        matches.add(ingredient);
                    }
                } else if (ingredient.test(itemStack)) {
                    matches.add(ingredient);
                    continue outer; // break;
                }
            }
        }
        if (matches.size() != ingredients.size()) return false;
        for (Object2IntMap.Entry<Integer> entry : requires2Count.object2IntEntrySet()) {
            var customIngredient = ingredients.get(entry.getKey());
            if (((AmountIngredient) customIngredient).getCount() > entry.getIntValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess access) {
        return this.getResultItem(access);
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
        return this.id;
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
//            HashSet<Item> items = new HashSet<>();
            for (int i = 0; i < ingredients.size(); ++i) {
                JsonElement jsonElement = ingredients.get(i);
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement.getAsJsonObject(), "amount");
                AmountIngredient ingredient = AmountIngredient.Serializer.INSTANCE.parse(json);
//                Item item = ingredient.getItem();
                nonNullList.set(i, ingredient);
//                if (items.add(item)) {
//                    nonNullList.set(i, ingredient);
//                } else {
//                    throw new IllegalArgumentException("Duplicate ingredient " + item);
//                }
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