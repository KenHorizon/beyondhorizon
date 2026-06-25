package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.inventory.VoidBagMenu;
import com.kenhorizon.beyondhorizon.server.item.VoidBagItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VoidBagCap implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    protected ItemStack itemStack;
    protected final LazyOptional<ItemStackHandler> handler;

    public VoidBagCap(ItemStack itemStack, CompoundTag nbt) {
        this.itemStack = itemStack;
        this.handler = LazyOptional.of(() -> new ItemStackHandler(VoidBagMenu.SLOTS));
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = handler.resolve().get().serializeNBT();
        this.itemStack.getOrCreateTag().put(VoidBagItem.NBT_BAG, tag);
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag tagCopy = this.itemStack.getOrCreateTag().getCompound(VoidBagItem.NBT_BAG);
        handler.resolve().get().deserializeNBT(tagCopy);
    }
}
