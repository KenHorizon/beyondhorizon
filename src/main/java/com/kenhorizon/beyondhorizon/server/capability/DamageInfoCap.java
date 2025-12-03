package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageInfoCap implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static ResourceLocation NAME = BeyondHorizon.resource("damage_info");
    protected final LazyOptional<DamageInfo> handler = LazyOptional.of(DamageInfo::new);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.DAMAGE_INFOS.orEmpty(cap, handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        return handler.orElseThrow(NullPointerException::new).serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        handler.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
    }
    public static boolean canAttachTo(ICapabilityProvider entity) {
        return entity instanceof LivingEntity;
    }
}