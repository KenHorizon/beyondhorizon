package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagHandler;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class StackableTagCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final LazyOptional<StackableTagHandler> handler;

    private final LivingEntity entity;

    public StackableTagCap(LivingEntity entity) {
        this.entity = entity;
        this.handler = LazyOptional.of(StackableTagHandler::new);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.STACK_TAGS.orEmpty(cap, this.handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.handler.orElseThrow(NullPointerException::new).serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.handler.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
    }

    public static boolean canAttachTo(ICapabilityProvider entity) {
        return entity instanceof LivingEntity;
    }
}