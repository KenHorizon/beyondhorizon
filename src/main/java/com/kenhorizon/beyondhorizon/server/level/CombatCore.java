package com.kenhorizon.beyondhorizon.server.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

public class CombatCore implements ICombatCore {
    public int duration;
    public boolean inCombat;
    private final int COMBAT_DURATION_TIMEOUT = 5;

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
        nbt.putInt("duration", this.duration);
        nbt.putBoolean("in_combat", this.inCombat);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.duration = nbt.getInt("duration");
        this.inCombat = nbt.getBoolean("in_combat");
    }
}
