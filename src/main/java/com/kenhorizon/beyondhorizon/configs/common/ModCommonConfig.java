package com.kenhorizon.beyondhorizon.configs.common;

import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch.AnvilCostSettings;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModCommonConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ModCommonConfig INSTANCE;


    static {
        Pair<ModCommonConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ModCommonConfig::new);
        SPEC = common.getRight();
        INSTANCE = common.getLeft();
    }

    public ModCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Beyond Horizon: Common Configs");
        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
}
