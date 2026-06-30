package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.server.capability.QuiverCap;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.capability.VoidBagCap;
import com.kenhorizon.beyondhorizon.server.inventory.QuiverMenu;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class QuiverItem extends BasicItem {
    public enum SlotType {
        UNDEFINED,
        MAIN_HAND,
        OFF_HAND,
        HOTBAR
    }
    public static final String NBT_QUIVERS = "quiver";
    public static int CONTAINER_SIZE = 5;

    public QuiverItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        SlotType slotType = hand == InteractionHand.OFF_HAND ? SlotType.OFF_HAND : SlotType.MAIN_HAND;
        if (!level.isClientSide()) {
            this.playInsertSound(player);
            NetworkHooks.openScreen((ServerPlayer) player, new ContainerProvider(itemStack), buf -> {
                buf.writeInt(-1);
                buf.writeEnum(slotType);
            });
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.consume(itemStack);
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
