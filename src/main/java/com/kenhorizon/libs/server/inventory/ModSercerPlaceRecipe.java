package com.kenhorizon.libs.server.inventory;

import com.kenhorizon.libs.server.item.recipe.AmountIngredient;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundExtendedPlacedRecipePacket;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class ModSercerPlaceRecipe<C extends Container> {
    public static final Logger LOGGER = LogUtils.getLogger();
    protected final StackedContents stackedContents = new StackedContents();
    protected Inventory inventory;
    protected ExtendedRecipeBookMenu<C> menu;

    public ModSercerPlaceRecipe(ExtendedRecipeBookMenu<C> bookMenu) {
        this.menu = bookMenu;
    }

    public void recipeClicked(ServerPlayer player, @Nullable Recipe<C> recipe, boolean placeAll) {
        if (recipe == null) return;
        this.inventory = player.getInventory();

        int batches = placeAll ? this.computeMaxBatches(recipe) : 1;
        if (batches < 1) {
            this.clearGrid();
            NetworkHandler.sendToClient(new ClientboundExtendedPlacedRecipePacket(player.containerMenu.containerId, recipe));
            player.getInventory().setChanged();
            return;
        }

        this.clearGrid();

        List<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (ingredient.isEmpty()) continue;

            int requiredCount = (ingredient instanceof AmountIngredient amt) ? amt.getCount() : 1;
            int needed = requiredCount * batches;

            ItemStack gathered = this.takeFromInventory(ingredient, needed);
            if (!gathered.isEmpty()) {
                Slot slot = this.menu.getSlot(gridSlotForIngredient(i));
                slot.set(gathered);
            }
        }

        player.getInventory().setChanged();
    }

    protected int computeMaxBatches(Recipe<C> recipe) {
        int maxBatches = Integer.MAX_VALUE;
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.isEmpty()) continue;
            int requiredCount = (ingredient instanceof AmountIngredient amt) ? amt.getCount() : 1;
            int available = countInInventory(ingredient);
            maxBatches = Math.min(maxBatches, available / Math.max(requiredCount, 1));
            if (maxBatches <= 0) return 0;
        }
        return maxBatches == Integer.MAX_VALUE ? 0 : maxBatches;
    }

    protected int countInInventory(Ingredient ingredient) {
        int total = 0;
        for (ItemStack stack : this.inventory.items) {
            if (!stack.isEmpty() && matchesType(ingredient, stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    protected ItemStack takeFromInventory(Ingredient ingredient, int needed) {
        ItemStack result = ItemStack.EMPTY;
        int remaining = needed;

        for (int i = 0; i < this.inventory.items.size() && remaining > 0; i++) {
            ItemStack stack = this.inventory.items.get(i);
            if (stack.isEmpty() || !matchesType(ingredient, stack)) continue;

            int take = Math.min(remaining, stack.getCount());
            if (result.isEmpty()) {
                result = stack.copyWithCount(take);
            } else {
                result.grow(take);
            }
            stack.shrink(take);
            remaining -= take;
        }

        if (remaining > 0) {
            if (!result.isEmpty()) this.inventory.placeItemBackInInventory(result, false);
            return ItemStack.EMPTY;
        }
        return result;
    }

    protected boolean matchesType(Ingredient ingredient, ItemStack stack) {
        if (ingredient instanceof AmountIngredient amt) {
            ItemStack template = amt.getItemStack();
            return template.hasTag()
                    ? ItemStack.isSameItemSameTags(stack, template)
                    : ItemStack.isSameItem(stack, template);
        }
        return ingredient.test(stack);
    }

    protected void clearGrid() {
        for (int i = 0; i < this.menu.getSize(); ++i) {
            if (this.menu.shouldMoveToInventory(i)) {
                ItemStack itemstack = this.menu.getSlot(i).getItem().copy();
                this.inventory.placeItemBackInInventory(itemstack, false);
                this.menu.getSlot(i).set(itemstack);
            }
        }
        this.menu.clearCraftingContent();
    }

    protected int gridSlotForIngredient(int ingredientIndex) {
        int seen = 0;
        for (int j = 0; j < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++j) {
            if (j != this.menu.getResultSlotIndex()) {
                if (seen == ingredientIndex) {
                    return j;
                }
                seen++;
            }
        }
        throw new IndexOutOfBoundsException("No grid slot for ingredient index " + ingredientIndex);
    }
}