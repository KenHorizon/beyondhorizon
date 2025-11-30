package com.kenhorizon.beyondhorizon.server.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

import javax.annotation.Nonnull;

public class BrewingRecipes extends BrewingRecipe {
    private final Ingredient input;

    public BrewingRecipes(Ingredient input, Ingredient ingredient, ItemStack output) {
        super(input, ingredient, output);
        this.input = input;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        if (stack == null) {
            return false;
        } else {
            ItemStack[] matchingStacks = input.getItems();
            if (matchingStacks.length == 0) {
                return stack.isEmpty();
            } else {
                for (ItemStack itemstack : matchingStacks) {
                    if (ItemStack.isSameItemSameTags(itemstack, stack)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
