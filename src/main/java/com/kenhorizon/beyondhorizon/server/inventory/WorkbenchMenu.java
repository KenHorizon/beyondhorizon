package com.kenhorizon.beyondhorizon.server.inventory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.item.recipe.WorkbenchRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

public class WorkbenchMenu extends ExtendedRecipeBookMenu<CraftingContainer> {
    private static final int CRAFTING_WIDTH = 5;
    private static final int CRAFTING_HEIGHT = 3;
    private final ContainerLevelAccess access;
    private final ResultContainer resultSlots = new ResultContainer();
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, CRAFTING_WIDTH, CRAFTING_HEIGHT);
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
    private final Player player;
    public List<WorkbenchRecipe> recipes = Lists.newArrayList();

    public WorkbenchMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }
    public WorkbenchMenu(int id, Inventory inventory, final ContainerLevelAccess access) {
        super(BHMenu.WORKBENCH_MENU.get(), id);
        this.access = access;
        this.level = inventory.player.level();
        this.player = inventory.player;
        this.recipes = level.getRecipeManager().getAllRecipesFor(WorkbenchRecipe.Type.getInstance());
        this.addSlot(new ResultSlot(this.player, this.craftSlots, this.resultSlots, 0, 139, 23));
        this.drawCrafingSlots(9, 9);

        // Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // Hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
        this.addDataSlot(this.selectedRecipeIndex);
    }

    public void drawCrafingSlots(int x, int y) {
        for (int w = 0; w < CRAFTING_HEIGHT; ++w) {
            for (int h = 0; h < CRAFTING_WIDTH; ++h) {
                this.addSlot(new Slot(this.craftSlots, h + w * CRAFTING_WIDTH, x + h * 18, y + w * 18));
            }
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }

//    @Override
//    public void handlePlacement(boolean pPlaceAll, Recipe<?> recipe, ServerPlayer serverPlayer) {
//        new ModifiedServerPlaceRecipe<>(this).recipeClicked(serverPlayer, (Recipe<CraftingContainer>) recipe, pPlaceAll);
//    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index == 1) {
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(RecipeType.STONECUTTING, new SimpleContainer(itemstack1), this.level).isPresent()) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level pLevel,
                                                  Player player, CraftingContainer container, ResultContainer result) {
        if (!pLevel.isClientSide()) {
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<WorkbenchRecipe> optional = pLevel.getServer().getRecipeManager().getRecipeFor(WorkbenchRecipe.Type.getInstance(), container, pLevel);
            if (optional.isPresent()) {
                WorkbenchRecipe craftingrecipe = optional.get();
                if (result.setRecipeUsed(pLevel, serverplayer, craftingrecipe)) {
                    ItemStack itemstack1 = craftingrecipe.assemble(container, pLevel.registryAccess());
                    if (itemstack1.isItemEnabled(pLevel.enabledFeatures())) {
                        stack = itemstack1;
                    }
                }
            }

            result.setItem(0, stack);
            menu.setRemoteSlot(0, stack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, stack));
        }
    }

    @Override
    public void slotsChanged(Container inv) {
        this.access.execute((level, blockPos) -> {
            slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BHBlocks.WORKBENCH.get());
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
        this.craftSlots.fillStackedContents(itemHelper);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftSlots, this.player.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return (CRAFTING_WIDTH * CRAFTING_HEIGHT) + 1;
    }

    @Override
    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != this.getResultSlotIndex();
    }
}
