package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items.QuiverTooltip;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items.VoidBagTooltip;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.capability.QuiverCap;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.capability.VoidBagCap;
import com.kenhorizon.beyondhorizon.server.inventory.QuiverMenu;
import com.kenhorizon.beyondhorizon.server.inventory.VoidBagMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class QuiverItem extends BasicItem {
    public enum SlotType {
        UNDEFINED,
        MAIN_HAND,
        OFF_HAND,
        HOTBAR
    }
    public static final String NBT_QUIVERS = "quiver";
    public static final String NBT_ITEM_SLOT = "Slot";
    public static int CONTAINER_SIZE = 5;

    public QuiverItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        SlotType slotType = hand == InteractionHand.OFF_HAND ? SlotType.OFF_HAND : SlotType.MAIN_HAND;
        if (!level.isClientSide()) {
            if (!player.isShiftKeyDown()) {
                this.playInsertSound(player);
                NetworkHooks.openScreen((ServerPlayer) player, new ContainerProvider(itemStack), buf -> {
                    buf.writeInt(-1);
                    buf.writeEnum(slotType);
                });
            } else {
                itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handlers -> {
                    if (handlers instanceof QuiverItemStackHandler quiverItemStackHandler) {
                        boolean ammoCollect = !quiverItemStackHandler.isAmmoCollect();
                        quiverItemStackHandler.setAmmoCollect(ammoCollect);
                        String collectStatus = ammoCollect ? "enabled" : "disabled";
                        ChatFormatting collectColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
                        player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_AMMO_COLLECT, Utils.capitalize(collectStatus)).withStyle(collectColour), true);

                    }
                });
            }
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null)  {
            QuiverItemStackHandler handler = (QuiverItemStackHandler) (stack.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElseThrow());
            handler.deserializeNBT(nbt.getCompound(NBT_QUIVERS));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        ListTag list = itemStack.getOrCreateTag().getCompound(NBT_QUIVERS).getList("Items", Tag.TAG_COMPOUND);
        NonNullList<ItemStack> items = NonNullList.withSize(QuiverMenu.SIZE, ItemStack.EMPTY);
        for(int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            ItemStack slotStack = ItemStack.of(tag);
            int slot = tag.getInt(NBT_ITEM_SLOT);
            items.set(slot, slotStack);
        }
        return Optional.of(new QuiverTooltip(items));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handlers -> {
            if (handlers instanceof QuiverItemStackHandler quiverItemStackHandler) {
                boolean ammoCollect = quiverItemStackHandler.isAmmoCollect();
                String collectStatus = ammoCollect ? "enabled" : "disabled";
                ChatFormatting collectColour = ammoCollect ? ChatFormatting.GREEN : ChatFormatting.RED;
                tooltip.add(Component.translatable(Tooltips.TOOLTIP_AMMO_COLLECT, Utils.capitalize(collectStatus)).withStyle(collectColour));

            }
        });
        super.appendHoverText(itemStack, level, tooltip, flag);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new QuiverCap(stack, nbt, CONTAINER_SIZE);
    }

    public boolean isAmmoValid(ItemStack pickedUpStack, ItemStack quiver) {
        return pickedUpStack.is(ItemTags.ARROWS);
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
            return new QuiverMenu(id, inventory, itemStack);
        }
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
