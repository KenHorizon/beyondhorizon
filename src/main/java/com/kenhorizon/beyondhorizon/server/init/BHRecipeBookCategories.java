package com.kenhorizon.beyondhorizon.server.init;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BHRecipeBookCategories {
    public static final RecipeBookCategories WORKBENCH_SEARCH = RecipeBookCategories.create("WORKBENCH_SEARCH", new ItemStack(Items.COMPASS));
    public static final RecipeBookCategories WORKBENCH = RecipeBookCategories.create("WORKBENCH", new ItemStack(BHItems.DORAN_BLADE.get()), new ItemStack(BHItems.DORAN_SHIELD.get()));
}
