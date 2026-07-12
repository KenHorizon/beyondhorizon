package com.kenhorizon.beyondhorizon.server.capability;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.IStackableInstance;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagHandler;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class StackableTagCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final LazyOptional<StackableTagHandler> handler = LazyOptional.of(StackableTagHandler::new);

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