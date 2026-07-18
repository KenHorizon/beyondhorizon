package com.kenhorizon.libs.server.inventory;

import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.Iterator;

public interface IModPlaceRecipe<T> {
    default void placeRecipe(int width, int height, int outputSlot, Recipe<?> recipes, Iterator<T> ingredients, int maxAmount) {
        int width1 = width;
        int height1 = height;
        if (recipes instanceof IShapedRecipe<?> shapedrecipe) {
            width1 = shapedrecipe.getRecipeWidth();
            height1 = shapedrecipe.getRecipeHeight();
        }

        int outputIndex = 0;

        for(int y = 0; y < height; ++y) {
            if (outputIndex == outputSlot) {
                ++outputIndex;
            }

            boolean flag = (float) height1 < (float) height / 2.0F;
            int l = Mth.floor((float)height / 2.0F - (float)height1 / 2.0F);
            if (flag && l > y) {
                outputIndex += width;
                ++y;
            }

            for (int x = 0; x < width; ++x) {
                if (!ingredients.hasNext()) {
                    return;
                }

                flag = (float)width1 < (float)width / 2.0F;
                l = Mth.floor((float)width / 2.0F - (float)width1 / 2.0F);
                int j1 = width1;
                boolean canAddToSlots = x < width1;
                if (flag) {
                    j1 = l + width1;
                    canAddToSlots = l <= x && x < l + width1;
                }

                if (canAddToSlots) {
                    this.addItemToSlot(recipes, ingredients, outputIndex, maxAmount, y, x);
                } else if (j1 == x) {
                    outputIndex += width - x;
                    break;
                }

                ++outputIndex;
            }
        }

    }

    void addItemToSlot(Recipe<?> recipes, Iterator<T> ingredients, int slot, int maxAmount, int y, int x);
}
