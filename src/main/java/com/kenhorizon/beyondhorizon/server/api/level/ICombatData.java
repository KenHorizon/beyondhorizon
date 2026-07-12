package com.kenhorizon.beyondhorizon.server.api.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICombatData extends INBTSerializable<CompoundTag> {

    public void activated();

    public int getDuration();

    public boolean OnCombat();

    public void tick();
}
