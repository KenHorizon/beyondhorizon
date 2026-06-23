package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.server.capability.VoidBagCap;
import com.kenhorizon.beyondhorizon.server.inventory.VoidBagMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class VoidBagItem extends BasicItem {
    public VoidBagItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            NetworkHooks.openScreen((ServerPlayer) player, new ContainerProvider(stack));
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, hand);
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
