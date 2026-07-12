package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.inventory.AccessoryStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class AccessoryInventoryCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    final Player wearer;
    private final LazyOptional<AccessoryStackHandler> optional;

    public AccessoryInventoryCap(final Player player) {
        this.wearer = player;
        this.optional = LazyOptional.of(() -> new AccessoryStackHandler(6));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.ACCESSORY.orEmpty(cap, optional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.optional.resolve().get().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.optional.resolve().get().deserializeNBT(nbt);
    }

    public static boolean canAttachTo(ICapabilityProvider entity) {
        return entity instanceof Player;
    }
}