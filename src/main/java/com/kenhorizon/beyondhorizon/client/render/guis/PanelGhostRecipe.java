package com.kenhorizon.beyondhorizon.client.render.guis;

import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.GhostRecipe;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PanelGhostRecipe {
    @Nullable
    private Recipe<?> recipe;
    private final List<PanelGhostRecipe.GhostIngredient> ingredients = Lists.newArrayList();
    float time;

    public void clear() {
        this.recipe = null;
        this.ingredients.clear();
        this.time = 0.0F;
    }

    public void addIngredient(Ingredient ingredient, int x, int y) {
        this.ingredients.add(new PanelGhostRecipe.GhostIngredient(ingredient, x, y));
    }

    public PanelGhostRecipe.GhostIngredient get(int index) {
        return this.ingredients.get(index);
    }

    public int size() {
        return this.ingredients.size();
    }

    @Nullable
    public Recipe<?> getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe<?> recipe) {
        this.recipe = recipe;
    }

    public void render(GuiGraphics guiGraphics, Minecraft minecraft, int x, int y, float partialTick) {
        if (!Screen.hasControlDown()) {
            this.time += partialTick;
        }

        for (int i = 0; i < this.ingredients.size(); ++i) {
            PanelGhostRecipe.GhostIngredient ingredient = this.ingredients.get(i);
            int j = ingredient.getX() + x;
            int k = ingredient.getY() + y;
            if (i == 0) {
                guiGraphics.fill(j - 4, k - 4, j + 20, k + 20, 822018048);
            } else {
                guiGraphics.fill(j, k, j + 16, k + 16, 822018048);
            }

            ItemStack itemstack = ingredient.getItem();
            guiGraphics.renderFakeItem(itemstack, j, k);
            guiGraphics.renderItemDecorations(minecraft.font, itemstack, j, k);
            guiGraphics.fill(RenderType.guiGhostRecipeOverlay(), j, k, j + 16, k + 16, 822083583);
            if (i == 0) {
                guiGraphics.renderItemDecorations(minecraft.font, itemstack, j, k);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class GhostIngredient {
        private final Ingredient ingredient;
        private final int x;
        private final int y;

        public GhostIngredient(Ingredient ingredient, int x, int y) {
            this.ingredient = ingredient;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public ItemStack getItem() {
            ItemStack[] aitemstack = this.ingredient.getItems();
            return aitemstack.length == 0 ? ItemStack.EMPTY : aitemstack[Mth.floor(PanelGhostRecipe.this.time / 30.0F) % aitemstack.length];
        }
    }
}
