package com.kenhorizon.beyondhorizon.configs.client;

import com.kenhorizon.beyondhorizon.client.level.guis.hud.GameHuds;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModClientConfig {
    public static ForgeConfigSpec.BooleanValue ADVANCED_TOOLTIP_ACCESSORY;
    public static ForgeConfigSpec.BooleanValue ADVANCED_TOOLTIP_SKILL;
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
        ADVANCED_TOOLTIP = builder
                .comment("Extend item's tooltip information")
                .define("Show Advanced Tooltip", true);
        ADVANCED_TOOLTIP_ACCESSORY = builder
                .comment("Extend item's tooltip information only in accessory")
                .define("Show Advanced Tooltip On Accessory", true);
        ADVANCED_TOOLTIP_SKILL = builder
                .comment("Extend item's tooltip information only in accessory")
                .define("Show Advanced Tooltip On Skill", true);
        GAME_HUD = builder
                .comment("Change how your game's hud will be displayed")
                .defineEnum("In-Game Hud", GameHuds.MOD, GameHuds.VANILLA, GameHuds.MOD);
        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, SPEC);
    }
}
