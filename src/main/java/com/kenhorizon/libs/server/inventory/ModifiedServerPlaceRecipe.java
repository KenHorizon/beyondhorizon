package com.kenhorizon.libs.server.inventory;

import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
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
    public static final Logger LOGGER = LogUtils.getLogger();
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
                BeyondHorizon.LOGGER.debug("Recipe Clicked stack content={}", this.stackedContents.contents);
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

        LOGGER.debug("[Modified Place Recipe:DEBUG] matches={} | recipe={}", flag, recipe.getId());
        int invStackCount = this.stackedContents.getBiggestCraftableStack(recipe, (IntList)null);
        if (flag) {
            for (int j = 0; j < this.menu.getGridHeight() * this.menu.getGridWidth() + 1; ++j) {
                if (j != this.menu.getResultSlotIndex()) {
                    ItemStack itemstack = this.menu.getSlot(j).getItem();
                    if (!itemstack.isEmpty() && Math.min(invStackCount, itemstack.getMaxStackSize()) < itemstack.getCount() + 1) {
                        return;
                    }
                }
            }
        }
        int stackSize = this.getStackSize(placeAll, invStackCount, flag);
        IntList intlist = new IntArrayList();
        if (this.stackedContents.canCraft(recipe, intlist, stackSize)) {
            int stackSize1 = stackSize;

            for (int index : intlist) {
                int fromStackingIndex = StackedContents.fromStackingIndex(index).getMaxStackSize();
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
        LOGGER.debug("[Modified Place Recipe:DEBUG] placeAll={}, maxPossible={}, matches={}", placeAll, maxPossible, matches);
        int stackSize = 1;
        if (placeAll) {
            stackSize = maxPossible;
        } else if (matches) {
            stackSize = 64;
            for (int j = 0; j < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++j) {
                if (j != this.menu.getResultSlotIndex()) {
                    ItemStack itemstack = this.menu.getSlot(j).getItem();
                    if (!itemstack.isEmpty() && stackSize > itemstack.getCount()) {
                        stackSize = itemstack.getCount();
                    }
                }
            }
            if (stackSize < 64) {
                ++stackSize;
            }
        }
        return stackSize;
    }

    protected void moveItemToGrid(Slot slotToFill, ItemStack stack) {
        int count = stack.getCount();
        int i = this.inventory.findSlotMatchingUnusedItem(stack);
        if (i != -1) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getCount() > count) {
                    this.inventory.removeItem(i, count);
                } else {
                    this.inventory.removeItemNoUpdate(i);
                }

                if (slotToFill.getItem().isEmpty()) {
                    slotToFill.set(itemstack.copyWithCount(count));
                } else {
                    slotToFill.getItem().grow(count);
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