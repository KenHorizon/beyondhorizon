package com.kenhorizon.beyondhorizon.server.level.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WorldData extends SavedData {
    private static final String IDENTIFIER = "BeyondHorizonWoldData";
    private static final String NBT_ENDER_DRAGON_IS_DEFEATED = "EnderDragon";
    private static final String NBT_BLAZING_INFERNO_IS_DEFEATED = "BlazingInferno";
    private boolean blazingInfernoDefeated = false;
    private boolean enderDragonDefeated = false;

    public WorldData() {
        super();
    }

    public static WorldData get(Level level, ResourceKey<Level> dim) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = level.getServer().getLevel(dim);
            if (serverLevel != null) {
                DimensionDataStorage storage = serverLevel.getDataStorage();
                WorldData data = storage.computeIfAbsent(WorldData::load, WorldData::new, IDENTIFIER);
                data.setDirty();
                return data;
            }
        }
        return null;
    }
    public static WorldData load(CompoundTag nbt) {
        WorldData data = new WorldData();
        data.setEnderDragonIsDefeated(nbt.getBoolean(NBT_BLAZING_INFERNO_IS_DEFEATED));
        data.setBlazingInfernoIsDefeated(nbt.getBoolean(NBT_BLAZING_INFERNO_IS_DEFEATED));
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean(NBT_ENDER_DRAGON_IS_DEFEATED, this.isEnderDragonDefeated());
        nbt.putBoolean(NBT_BLAZING_INFERNO_IS_DEFEATED, this.isBlazingInfernoDefeated());
        return nbt;
    }

    public void setBlazingInfernoIsDefeated(boolean defeated) {
        this.blazingInfernoDefeated = defeated;
    }

    public boolean isBlazingInfernoDefeated() {
        return this.blazingInfernoDefeated;
    }
    public void setEnderDragonIsDefeated(boolean defeated) {
        this.enderDragonDefeated = defeated;
    }

    public boolean isEnderDragonDefeated() {
        return this.enderDragonDefeated;
    }
}
