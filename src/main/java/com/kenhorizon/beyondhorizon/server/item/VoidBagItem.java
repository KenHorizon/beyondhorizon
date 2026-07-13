package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.server.item.tooltips.VoidBagTooltip;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.capability.VoidBagCap;
import com.kenhorizon.beyondhorizon.server.inventory.VoidBagMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VoidBagItem extends BasicItem {
    public enum SlotType {
        UNDEFINED,
        MAIN_HAND,
        OFF_HAND,
        HOTBAR
    }
    public static final String NBT_BAG = "Bag";
    public static final String NBT_ITEM_SLOT = "Slot";
    public VoidBagItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {

            SlotType slotType = hand == InteractionHand.OFF_HAND ? SlotType.OFF_HAND : SlotType.MAIN_HAND;
            NetworkHooks.openScreen((ServerPlayer) player, new ContainerProvider(stack), buf -> {
                buf.writeEnum(slotType);
                buf.writeInt(-1);
            });
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null)  {
            QuiverItemStackHandler handler = (QuiverItemStackHandler) (stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElseThrow());
            handler.deserializeNBT(nbt.getCompound(VoidBagItem.NBT_BAG));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        ListTag list = itemStack.getOrCreateTag().getCompound(NBT_BAG).getList("Items", Tag.TAG_COMPOUND);
        NonNullList<ItemStack> items = NonNullList.withSize(VoidBagMenu.SLOTS, ItemStack.EMPTY);
        for(int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            ItemStack slotStack = ItemStack.of(tag);
            int slot = tag.getInt(NBT_ITEM_SLOT);
            items.set(slot, slotStack);
        }
        return Optional.of(new VoidBagTooltip(items));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new VoidBagCap(stack, nbt);
    }

    protected class ContainerProvider implements MenuProvider {
        protected final ItemStack itemStack;

        protected ContainerProvider(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public Component getDisplayName() {
            return Component.empty();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
            return new VoidBagMenu(id, inventory, this.itemStack);
        }
    }
}
