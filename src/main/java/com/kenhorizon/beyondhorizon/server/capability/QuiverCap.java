package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.item.QuiverItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuiverCap implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    protected ItemStack quiver;
    protected final LazyOptional<QuiverItemStackHandler> handler;

    public QuiverCap(ItemStack quiver, CompoundTag nbt, int containerSize) {
        this.quiver = quiver;
        this.handler = LazyOptional.of(() -> new QuiverItemStackHandler(containerSize));
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = this.handler.resolve().get().serializeNBT();
        this.quiver.getOrCreateTag().put(QuiverItem.NBT_QUIVERS, tag);
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag tagCopy = this.quiver.getOrCreateTag().getCompound(QuiverItem.NBT_QUIVERS);
        this.handler.resolve().get().deserializeNBT(tagCopy);
    }
}
