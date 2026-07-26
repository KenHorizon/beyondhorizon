package com.kenhorizon.beyondhorizon.server.level.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class BossWorldData extends SavedData {
    private static final String IDENTIFIER = "beyondhorizon_boss_wold_data";
    private static final String NBT_ENDER_DRAGON_IS_DEFEATED = "ender_dragon";
    private static final String NBT_WITHER_BOSS_IS_DEFEATED = "wither_boss";
    private static final String NBT_BLAZING_INFERNO_IS_DEFEATED = "blazing_inferno";
    private boolean witherBossDefeated = false;
    private boolean blazingInfernoDefeated = false;
    private boolean enderDragonDefeated = false;

    public BossWorldData() {
        super();
    }

    public static BossWorldData get(Level level, ResourceKey<Level> dim) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = level.getServer().getLevel(dim);
            if (serverLevel != null) {
                DimensionDataStorage storage = serverLevel.getDataStorage();
                BossWorldData data = storage.computeIfAbsent(BossWorldData::load, BossWorldData::new, IDENTIFIER);
                data.setDirty();
                return data;
            }
        }
        return null;
    }
    public static BossWorldData load(CompoundTag nbt) {
        BossWorldData data = new BossWorldData();
        data.setEnderDragonIsDefeated(nbt.getBoolean(NBT_BLAZING_INFERNO_IS_DEFEATED));
        data.setBlazingInfernoIsDefeated(nbt.getBoolean(NBT_BLAZING_INFERNO_IS_DEFEATED));
        data.setWitherBossIsDefeated(nbt.getBoolean(NBT_WITHER_BOSS_IS_DEFEATED));
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean(NBT_WITHER_BOSS_IS_DEFEATED, this.isWitherBossDefeated());
        nbt.putBoolean(NBT_ENDER_DRAGON_IS_DEFEATED, this.isEnderDragonDefeated());
        nbt.putBoolean(NBT_BLAZING_INFERNO_IS_DEFEATED, this.isBlazingInfernoDefeated());
        return nbt;
    }

    public void setWitherBossIsDefeated(boolean defeated) {
        this.witherBossDefeated = defeated;
    }

    public boolean isWitherBossDefeated() {
        return this.witherBossDefeated;
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
