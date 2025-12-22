package com.kenhorizon.beyondhorizon.server.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

public class CombatCore implements ICombatCore {
    public int duration;
    public boolean inCombat;
    private final int COMBAT_DURATION_TIMEOUT = 5;
    public static final String NBT_DURATION = "Duration";
    public static final String NBT_INCOMBAT = "InCombat";

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setInCombat(boolean combat) {
        this.inCombat = combat;
    }

    @Override
    public void activated() {
        this.setDuration(this.COMBAT_DURATION_TIMEOUT * 20);
    }

    @Override
    public void tick() {
        this.setInCombat(this.getDuration() > 0);
        if (this.getDuration() > 0) {
            this.setDuration(this.getDuration() - 1);
        }
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public boolean OnCombat() {
        return this.inCombat;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(NBT_DURATION, this.duration);
        nbt.putBoolean(NBT_INCOMBAT, this.inCombat);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.duration = nbt.getInt(NBT_DURATION);
        this.inCombat = nbt.getBoolean(NBT_INCOMBAT);
    }
}
