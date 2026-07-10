package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkill;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkillItemCap implements ICapabilityProvider {

    public static SkillItemCap createProvider(final ISkill skill) {
        return new SkillItemCap(skill);
    }

    protected final LazyOptional<ISkill> capability;

    public SkillItemCap(ISkill cap) {
        this.capability = LazyOptional.of(() -> cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BHCapabilties.SKILL_ITEM.orEmpty(cap, this.capability.cast());
    }
}
