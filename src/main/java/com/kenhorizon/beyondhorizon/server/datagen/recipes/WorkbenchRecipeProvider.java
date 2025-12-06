package com.kenhorizon.beyondhorizon.server.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WorkbenchRecipeProvider implements RecipeBuilder {
    private final Item result;
    private final List<WorkbenchRecipe.Recipes> recipes = new ArrayList<>();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final RecipeSerializer<?> serializer;
    private final int count;

    public WorkbenchRecipeProvider(RecipeSerializer<?> serializer, ItemLike result, int count) {
        this.serializer = serializer;
        this.result = result.asItem();
        this.count = count;

    }

    public static WorkbenchRecipeProvider create(ItemLike result, int count) {
        return new WorkbenchRecipeProvider(WorkbenchRecipe.Serializer.getInstance(), result, count) ;
    }

    public static WorkbenchRecipeProvider create(ItemLike result) {
        return create(result, 1);
    }

    public WorkbenchRecipeProvider required(TagKey<Item> item, int count) {
        this.recipes.add(new WorkbenchRecipe.Recipes(Ingredient.of(item), count));
        return this;
    }

    public WorkbenchRecipeProvider required(ItemLike item) {
        this.recipes.add(new WorkbenchRecipe.Recipes(Ingredient.of(item.asItem()), 0));
        return this;
    }

    public WorkbenchRecipeProvider required(ItemStack item) {
        this.recipes.add(new WorkbenchRecipe.Recipes(Ingredient.of(item.getItem()), 0));
        return this;
    }

    public WorkbenchRecipeProvider required(ItemLike item, int count) {
        this.recipes.add(new WorkbenchRecipe.Recipes(Ingredient.of(item.asItem()), count));
        return this;
    }

    public WorkbenchRecipeProvider required(ItemStack item, int count) {
        this.recipes.add(new WorkbenchRecipe.Recipes(Ingredient.of(item.getItem()), count));
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        //this.ensureValid(recipeId);
        this.advancement.parent(ResourceLocation.parse("recipes/root"))
                .addCriterion("has_the_recipes", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        consumer.accept(new WorkbenchRecipeProvider.Result(recipeId, this.serializer, this.recipes, this.result, this.count, this.advancement, recipeId.withPrefix("recipes/")));
    }
    public static class Result implements FinishedRecipe {
        private ResourceLocation id;
        private final List<WorkbenchRecipe.Recipes> recipes;
        private final Item result;
        private final int count;
        private final RecipeSerializer<?> type;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, RecipeSerializer<?> recipeSerializer, List<WorkbenchRecipe.Recipes> recipes, Item result, int count, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.type = recipeSerializer;
            this.recipes = recipes;
            this.result = result;
            this.count = count;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredientList = new JsonArray();
            JsonArray countList = new JsonArray();

            for(WorkbenchRecipe.Recipes recipes : this.recipes) {
                ingredientList.add(recipes.ingredient().toJson());
                countList.add(recipes.count());
            }

            json.add("ingredients", ingredientList);
            json.add("counts", countList);
            JsonObject result = new JsonObject();
            result.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            result.addProperty("count", this.count);
            json.add("result", result);
        }

        @Override
        public ResourceLocation getId() {
            return this.id = BeyondHorizon.resource(
                    ForgeRegistries.ITEMS.getKey(this.result).getPath() + "_from_workbench");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.type;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
