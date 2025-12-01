package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AccessoryInventoryCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static ResourceLocation NAME = BeyondHorizon.resource("accessory");
    private final LazyOptional<AccessoryItemStackHandler> handler = LazyOptional.of(
            () -> new AccessoryItemStackHandler(6));

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.ACCESSORY.orEmpty(cap, handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.handler.resolve().get().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.handler.resolve().get().deserializeNBT(nbt);
    }

    public static boolean canAttachTo(ICapabilityProvider entity) {
        return entity instanceof Player;
    }
}