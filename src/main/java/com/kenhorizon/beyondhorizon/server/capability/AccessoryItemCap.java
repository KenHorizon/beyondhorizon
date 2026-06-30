package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
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

public class AccessoryItemCap implements ICapabilityProvider {

    public static AccessoryItemCap createProvider(final IAccessory accessory) {
        return new AccessoryItemCap(accessory);
    }
    protected final LazyOptional<IAccessory> capability;
    public AccessoryItemCap(IAccessory cap) {
        this.capability = LazyOptional.of(() -> cap);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.ACCESSORY_ITEM.orEmpty(cap, this.capability.cast());
    }
}
