package com.kenhorizon.beyondhorizon.server.inventory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHMenu;
import com.kenhorizon.beyondhorizon.server.item.QuiverItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class QuiverMenu extends AbstractContainerMenu {
    public static final int SIZE = 5;
    protected final ItemStack itemStack;
    protected final IItemHandler handler;
    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.parse("textures/atlas/blocks.png");
    public static final ResourceLocation EMPTY_QUIVER_SLOTS = BeyondHorizon.resource("item/slot/empty_quiver_slots");

    public static QuiverMenu createFromNetwork(int id, Inventory inventory, FriendlyByteBuf buf) {
        int slot = buf.readInt();
        ItemStack quiverStack = findQuiverStack(inventory, buf.readEnum(QuiverItem.SlotType.class), slot);
        return new QuiverMenu(id, inventory, quiverStack);
    }

    public QuiverMenu(int pContainerId, Inventory inventory, ItemStack itemStackIn) {
        super(BHMenu.QUIVER_MENU.get(), pContainerId);
        this.itemStack = itemStackIn;
        this.handler = itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElseThrow();
        for (int k = 0; k < SIZE; ++k) {
            this.addSlot(new SlotItemHandler(this.handler, k, 44 + k * 18, 13) {
                @Override
                public boolean mayPlace(ItemStack itemStack) {
                    return itemStack.is(ItemTags.ARROWS);
                }

                @Override
                public @NotNull Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(BLOCK_ATLAS, EMPTY_QUIVER_SLOTS);
                }
            });
        }
        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);
    }

    public IItemHandler getHandler() {
        return handler;
    }

    //
    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = SIZE;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // Slot: 27
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 51 + (i * 18)));
            }
        }
    }

    // Slot: 9
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }

    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player) {
        if (slot >= 0 && getSlot(slot) != null && (getSlot(slot).getItem().equals(this.itemStack, false))) return;
        super.clicked(slot, button, clickType, player);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    protected static ItemStack findQuiverStack(Inventory inventory, QuiverItem.SlotType slotType, int slot) {
        ItemStack quiverStack = ItemStack.EMPTY;
        InteractionHand itemHand = inventory.player.getUsedItemHand();
        switch (slotType) {
            case HOTBAR:
                quiverStack = inventory.getItem(slot);
                break;
            case MAIN_HAND:
                quiverStack = inventory.player.getMainHandItem();
                break;
            case OFF_HAND:
                quiverStack = inventory.player.getOffhandItem();
                break;
            default:
                break;
        }
        return quiverStack;
    }
}