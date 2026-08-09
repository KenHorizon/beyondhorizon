package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.tags.BHEntityTypeTags;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class LevelSystemsCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final LazyOptional<LevelSystem> handler;

    public LevelSystemsCap(LivingEntity entity) {
        this.handler =  LazyOptional.of(() -> new LevelSystem(entity));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.LEVEL_SYSTEM.orEmpty(cap, this.handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.handler.orElseThrow(NullPointerException::new).saveNbt();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.handler.orElseThrow(NullPointerException::new).loadNbt(nbt);
    }

    public static boolean canAttachTo(ICapabilityProvider cap) {
        return cap instanceof LivingEntity entity && !(entity.getType().is(Tags.EntityTypes.BOSSES)
                || entity.getType().is(BHEntityTypeTags.UNAFFECTED_BY_LEVELS));
    }
}