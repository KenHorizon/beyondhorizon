package com.kenhorizon.libs.server.inventory;

import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundExtendedPlacedRecipePacket;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ModifiedServerPlaceRecipe <C extends Container> implements PlaceRecipe<Integer> {
    protected final StackedContents stackedContents = new StackedContents();
    protected Inventory inventory;
    protected ExtendedRecipeBookMenu<C> menu;

    public ModifiedServerPlaceRecipe(ExtendedRecipeBookMenu<C> bookMenu) {
        this.menu = bookMenu;
    }

    public void recipeClicked(ServerPlayer player, @Nullable Recipe<C> recipe, boolean placeAll) {
        if (recipe != null) {
            this.inventory = player.getInventory();
            if (this.testClearGrid() || player.isCreative()) {
                this.stackedContents.clear();
                player.getInventory().fillStackedContents(this.stackedContents);
                this.menu.fillCraftSlotsStackedContents(this.stackedContents);
                if (this.stackedContents.canCraft(recipe, (IntList) null)) {
                    this.handleRecipeClicked(recipe, placeAll);
                } else {
                    this.clearGrid();
                    NetworkHandler.sendToClient(new ClientboundExtendedPlacedRecipePacket(player.containerMenu.containerId, recipe));
                }

                player.getInventory().setChanged();
            }
        }
    }

    protected void clearGrid() {
        for(int i = 0; i < this.menu.getSize(); ++i) {
            if (this.menu.shouldMoveToInventory(i)) {
                ItemStack itemstack = this.menu.getSlot(i).getItem().copy();
                this.inventory.placeItemBackInInventory(itemstack, false);
                this.menu.getSlot(i).set(itemstack);
            }
        }

        this.menu.clearCraftingContent();
    }

    protected void handleRecipeClicked(Recipe<C> recipe, boolean placeAll) {
        boolean flag = this.menu.recipeMatches(recipe);
        int i = this.stackedContents.getBiggestCraftableStack(recipe, (IntList)null);
        if (flag) {
            for(int j = 0; j < this.menu.getGridHeight() * this.menu.getGridWidth() + 1; ++j) {
                if (j != this.menu.getResultSlotIndex()) {
                    ItemStack itemstack = this.menu.getSlot(j).getItem();
                    if (!itemstack.isEmpty() && Math.min(i, itemstack.getMaxStackSize()) < itemstack.getCount() + 1) {
                        return;
                    }
                }
            }
        }

        int stackSize = this.getStackSize(placeAll, i, flag);
        IntList intlist = new IntArrayList();
        if (this.stackedContents.canCraft(recipe, intlist, stackSize)) {
            int stackSize1 = stackSize;

            for(int l : intlist) {
                int fromStackingIndex = StackedContents.fromStackingIndex(l).getMaxStackSize();
                if (fromStackingIndex < stackSize1) {
                    stackSize1 = fromStackingIndex;
                }
            }

            if (this.stackedContents.canCraft(recipe, intlist, stackSize1)) {
                this.clearGrid();
                this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, intlist.iterator(), stackSize1);
            }
        }

    }

    @Override
    public void addItemToSlot(Iterator<Integer> ingredients, int menuSlots, int max, int y, int x) {
        Slot slot = this.menu.getSlot(menuSlots);
        ItemStack itemstack = StackedContents.fromStackingIndex(ingredients.next());
        if (!itemstack.isEmpty()) {
            for(int i = 0; i < max; ++i) {
                this.moveItemToGrid(slot, itemstack);
            }
        }

    }

    protected int getStackSize(boolean placeAll, int maxPossible, boolean matches) {
        int i = 1;
        if (placeAll) {
            i = maxPossible;
        } else if (matches) {
            i = 64;

            for(int j = 0; j < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++j) {
                if (j != this.menu.getResultSlotIndex()) {
                    ItemStack itemstack = this.menu.getSlot(j).getItem();
                    if (!itemstack.isEmpty() && i > itemstack.getCount()) {
                        i = itemstack.getCount();
                    }
                }
            }

            if (i < 64) {
                ++i;
            }
        }

        return i;
    }

    protected void moveItemToGrid(Slot slotToFill, ItemStack stack) {
        int i = this.inventory.findSlotMatchingUnusedItem(stack);
        if (i != -1) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getCount() > 1) {
                    this.inventory.removeItem(i, 1);
                } else {
                    this.inventory.removeItemNoUpdate(i);
                }

                if (slotToFill.getItem().isEmpty()) {
                    slotToFill.set(itemstack.copyWithCount(1));
                } else {
                    slotToFill.getItem().grow(1);
                }

            }
        }
    }

    /**
     * Places the output of the recipe into the player's inventory.
     */
    private boolean testClearGrid() {
        List<ItemStack> list = Lists.newArrayList();
        int i = this.getAmountOfFreeSlotsInInventory();

        for(int j = 0; j < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++j) {
            if (j != this.menu.getResultSlotIndex()) {
                ItemStack itemstack = this.menu.getSlot(j).getItem().copy();
                if (!itemstack.isEmpty()) {
                    int k = this.inventory.getSlotWithRemainingSpace(itemstack);
                    if (k == -1 && list.size() <= i) {
                        for(ItemStack itemstack1 : list) {
                            if (ItemStack.isSameItem(itemstack1, itemstack) && itemstack1.getCount() != itemstack1.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                                itemstack1.grow(itemstack.getCount());
                                itemstack.setCount(0);
                                break;
                            }
                        }

                        if (!itemstack.isEmpty()) {
                            if (list.size() >= i) {
                                return false;
                            }

                            list.add(itemstack);
                        }
                    } else if (k == -1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private int getAmountOfFreeSlotsInInventory() {
        int i = 0;

        for(ItemStack itemstack : this.inventory.items) {
            if (itemstack.isEmpty()) {
                ++i;
            }
        }

        return i;
    }
}