package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.player.PlayerData;
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

public class PlayerDataCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static ResourceLocation NAME = BeyondHorizon.resource("player_data");
    private final LazyOptional<PlayerData> handler = LazyOptional.of(this::createInstance);
    private PlayerData instance = null;
    private Player player;

    public PlayerDataCap(Player player) {
        this.player = player;
    }

    private @NotNull PlayerData createInstance() {
        if (this.instance == null) {
            this.instance = new PlayerData(this.player);
        }
        return this.instance;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.PLAYER_DATA.orEmpty(cap, this.handler.cast());
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