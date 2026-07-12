package com.kenhorizon.libs.server.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Recipe;

public abstract class ExtendedRecipeBookMenu<C extends Container> extends AbstractContainerMenu {
    public ExtendedRecipeBookMenu(MenuType<?> menuType, int id) {
        super(menuType, id);
    }

    public void handlePlacement(boolean placeAll, Recipe<C> recipe, ServerPlayer serverPlayer) {
        new ModifiedServerPlaceRecipe<>(this).recipeClicked(serverPlayer, recipe, placeAll);
    }

    public abstract void fillCraftSlotsStackedContents(StackedContents itemHelper);

    public abstract void clearCraftingContent();

    public abstract boolean recipeMatches(Recipe<? super C> recipe);

    public abstract int getResultSlotIndex();

    public abstract int getGridWidth();

    public abstract int getGridHeight();

    public abstract int getSize();

    public abstract boolean shouldMoveToInventory(int slotIndex);
    }