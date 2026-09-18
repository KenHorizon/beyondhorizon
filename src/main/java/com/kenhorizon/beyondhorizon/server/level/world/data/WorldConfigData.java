package com.kenhorizon.beyondhorizon.server.level.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

//TODO: make a config screen in World Creation
public class WorldConfigData extends SavedData {
    private static final String IDENTIFIER = "beyondhorizon_woldconfig_data";
    private static final String OVERRIDE_DAMAGE_CALCULATION = "override_damage_calculation";
    private boolean overrideDamageCalculation = false;

    public static WorldConfigData get(Level level, ResourceKey<Level> dim) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = level.getServer().getLevel(dim);
            if (serverLevel != null) {
                DimensionDataStorage storage = serverLevel.getDataStorage();
                WorldConfigData data = storage.computeIfAbsent(WorldConfigData::load, WorldConfigData::new, IDENTIFIER);
                data.setDirty();
                return data;
            }
        }
        return null;
    }

    public static WorldConfigData load(CompoundTag nbt) {
        WorldConfigData data = new WorldConfigData();
        data.setOverrideDamageCalculation(nbt.getBoolean(OVERRIDE_DAMAGE_CALCULATION));
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean(OVERRIDE_DAMAGE_CALCULATION, this.isOverrideDamageCalculation());
        return nbt;
    }

    public void setOverrideDamageCalculation(boolean overrideDamageCalculation) {
        this.overrideDamageCalculation = overrideDamageCalculation;
    }

    public boolean isOverrideDamageCalculation() {
        return this.overrideDamageCalculation;
    }
}
