package com.kenhorizon.beyondhorizon.configs.client;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModClientConfig {
    public static ForgeConfigSpec.BooleanValue ADVANCED_TOOLTIP;
    public static ForgeConfigSpec.EnumValue GAME_HUD;

    private static final ForgeConfigSpec SPEC;
    public static final ModClientConfig INSTANCE;

    static {
        Pair<ModClientConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ModClientConfig::new);
        SPEC = common.getRight();
        INSTANCE = common.getLeft();
    }

    public ModClientConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("client_mod_configs").push("Beyond Horizon: Client Configs");

        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, SPEC);
    }
}
