package com.kenhorizon.beyondhorizon.server.inventory;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHMenu;
import com.kenhorizon.beyondhorizon.server.inventory.slot.AccessorySlot;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItemHandler;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AccessoryMenu extends InventoryMenu {
    public final Player player;
    public final Inventory inventory;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.parse("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.parse("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.parse("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.parse("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = ResourceLocation.parse("item/empty_armor_slot_shield");
    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.parse("textures/atlas/blocks.png");
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 2, 2);
    private final ResultContainer resultSlots = new ResultContainer();
    public final IAccessoryItemHandler handler;

    public AccessoryMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory);
    }

    public AccessoryMenu(int containerId, Inventory inventory) {
        super(inventory, inventory.player.level().isClientSide(), inventory.player);
        this.menuType = BHMenu.ACCESSORY_MENU.get();
        this.containerId = containerId;
        this.player = inventory.player;
        this.inventory = inventory;
        this.handler = CapabilityCaller.accessory(this.player);
        this.resultSlot(inventory);
        this.craftingGrid();
        this.addPlayerInventorySlot();
        this.accessorySlot();
    }

    private void resultSlot(Inventory inventory) {
        this.addSlot(new ResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, 154, 28));
    }

    private void craftingGrid() {
        for (int x = 0; x < 2; ++x) {
            for(int y = 0; y < 2; ++y) {
                this.addSlot(new Slot(this.craftSlots, y + x * 2, 98 + y * 18, 18 + x * 18));
            }
        }
    }

    private void addPlayerInventorySlot() {
        // Armor Slot
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            this.addSlot(new Slot(inventory, 39 - k, 8, 8 + k * 18) {
                @Override
                public void setByPlayer(ItemStack itemStack) {
                    onEquipItem(player, equipmentslot, itemStack, this.getItem());
                    super.setByPlayer(itemStack);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack itemStack) {
                    return itemStack.canEquip(equipmentslot, player);
                }

                @Override
                public boolean mayPickup(Player player) {
                    ItemStack itemstack = this.getItem();
                    return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(player);
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }
        //Inventory
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        //Hotbar
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }
        this.addSlot(new Slot(inventory, 40, 77, 62) {
            @Override
            public void setByPlayer(ItemStack itemStack) {
                onEquipItem(player, EquipmentSlot.OFFHAND, itemStack, this.getItem());
                super.setByPlayer(itemStack);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }


    private void accessorySlot() {
        for (int i = 0; i < this.handler.getSlots(); i++) {
            this.addSlot(new AccessorySlot(this.handler, i, -24, 8 + i * 18));
        }
    }

    private void onEquipItem(Player player, EquipmentSlot slot, ItemStack newItem, ItemStack oldItem) {
        Equipable equipable = Equipable.get(newItem);
        if (equipable != null) {
            player.onEquipItem(slot, oldItem, newItem);
        }
    }

    public boolean isOnHotbarSlot(int index) {
        return index >= 36 && index < 45 || index == 45;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack copyOfSourceStack = ItemStack.EMPTY;
        Slot sourceSlot = this.slots.get(index);
        if (sourceSlot.hasItem()) {
            ItemStack sourceStack = sourceSlot.getItem();
            copyOfSourceStack = sourceStack.copy();
            EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(copyOfSourceStack);
            if (index == 0) {
                if (!this.moveItemStackTo(sourceStack, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                sourceSlot.onQuickCraft(sourceStack, copyOfSourceStack);
            }
            // Check if the slot clicked is one of the vanilla container slots
            if (index >= 1 && index < 5) {
                if (!this.moveItemStackTo(sourceStack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 9) {
                if (!this.moveItemStackTo(sourceStack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - equipmentslot.getIndex()).hasItem()) {
                int i = 8 - equipmentslot.getIndex();
                if (!this.moveItemStackTo(sourceStack, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
                if (!this.moveItemStackTo(sourceStack, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 9 && index < 36) {
                if (!this.moveItemStackTo(sourceStack, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 && index < 45) {
                if (!this.moveItemStackTo(sourceStack, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 46) {
                if (!this.moveItemStackTo(sourceStack, 46, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(sourceStack, 9, 45, false)) {
                return ItemStack.EMPTY;
            }
            if (sourceStack.isEmpty()) {
                sourceSlot.setByPlayer(ItemStack.EMPTY);
            } else {
                sourceSlot.setChanged();
            }

            if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
                return ItemStack.EMPTY;
            }

            sourceSlot.onTake(playerIn, sourceStack);
            if (index == 0) {
                playerIn.drop(sourceStack, false);
            }
        }
        return copyOfSourceStack;
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        slotChangedCraftingGrid(this, this.player.level(), this.player, this.craftSlots, this.resultSlots);
    }

    protected void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (!level.isClientSide()) {
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
            if (optional.isPresent()) {
                CraftingRecipe craftingRecipe = optional.get();
                if (resultContainer.setRecipeUsed(level, serverplayer, craftingRecipe)) {
                    ItemStack assemble = craftingRecipe.assemble(craftingContainer, level.registryAccess());
                    if (assemble.isItemEnabled(level.enabledFeatures())) {
                        itemStack = assemble;
                    }
                }
            }

            resultContainer.setItem(0, itemStack);
            menu.setRemoteSlot(0, itemStack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemStack));
        }
    }
    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents contents) {
        this.craftSlots.fillStackedContents(contents);
    }

    @Override
    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        this.craftSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftSlots, this.player.level());
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(itemStack, slot);
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
    public @NotNull CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
}
