package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.api.skills.ISkillSlots;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillSlots;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkillSlotsCap implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    protected final LazyOptional<ISkillSlots> handler;

    public SkillSlotsCap(ISkillItems skillItems) {
        this.handler = LazyOptional.of(() -> new SkillSlots(skillItems));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.SKILL_SLOTS.orEmpty(cap, handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return handler.orElseThrow(NullPointerException::new).writeNbt();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        handler.orElseThrow(NullPointerException::new).loadNbt(nbt);
    }
}