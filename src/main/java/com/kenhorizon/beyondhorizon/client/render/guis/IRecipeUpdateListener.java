package com.kenhorizon.beyondhorizon.client.render.guis;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public interface IRecipeUpdateListener {

    void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots);



}
