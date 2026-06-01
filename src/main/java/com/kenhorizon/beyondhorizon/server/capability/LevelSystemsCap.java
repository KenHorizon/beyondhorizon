package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.classes.LevelSystem;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
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

public class LevelSystemsCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static ResourceLocation NAME = BeyondHorizon.resource("leyel_systems");
    private final LazyOptional<LevelSystem> handler = LazyOptional.of(LevelSystem::new);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.ROLE_CLASS.orEmpty(cap, this.handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.handler.orElseThrow(NullPointerException::new).saveNbt();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.handler.orElseThrow(NullPointerException::new).loadNbt(nbt);
    }

    public static boolean canAttachTo(ICapabilityProvider entity) {
        return entity instanceof Player;
    }
}