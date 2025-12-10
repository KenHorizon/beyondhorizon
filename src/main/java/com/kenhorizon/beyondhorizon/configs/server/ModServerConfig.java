package com.kenhorizon.beyondhorizon.configs.server;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModServerConfig {
    public static ForgeConfigSpec.BooleanValue BLOODMOON_SPAWN_FULLMOON;

    public static final ForgeConfigSpec SPEC;
    public static final ModServerConfig INSTANCE;


    static {
        Pair<ModServerConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ModServerConfig::new);
        SPEC = common.getRight();
        INSTANCE = common.getLeft();
    }

    public ModServerConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("server_mod_congifs").push("Beyond Horizon: Server Configs");

        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
}
