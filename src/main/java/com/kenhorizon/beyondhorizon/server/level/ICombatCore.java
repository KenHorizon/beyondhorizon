package com.kenhorizon.beyondhorizon.server.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICombatCore extends INBTSerializable<CompoundTag> {

    public void activated();

    public int getDuration();

    public boolean OnCombat();

    public void tick();
}
