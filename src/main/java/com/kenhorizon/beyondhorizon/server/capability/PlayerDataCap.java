package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlayerDataCap implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final LazyOptional<PlayerData> optional;
    private final Player player;

    public PlayerDataCap(Player player) {
        this.player = player;
        this.optional = LazyOptional.of(() -> new PlayerData(this.player));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.PLAYER_DATA.orEmpty(cap, this.optional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.optional.orElseThrow(NullPointerException::new).saveNbt();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.optional.orElseThrow(NullPointerException::new).loadNbt(nbt);
    }

    public static boolean canAttachTo(ICapabilityProvider entity) {
        return entity instanceof Player;
    }
}