package com.kenhorizon.beyondhorizon.configs;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.enums.GameHuds;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.configs.common.ModCommonConfig;
import com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch.AnvilCostSettings;
import net.minecraftforge.fml.config.ModConfig;

public class BHConfigs {
    public static int MOBS_MAX_LEVEL_CAP = 100;
    public static int MOBS_MIN_LEVEL_CAP = 5;

    public static int ACCESSORY_BUTTON_X = 0;
    public static int ACCESSORY_BUTTON_Y = 0;
    //CLIENT
    public static boolean ALWAYS_SHOW_SKILL_ABILITY = true;
    public static boolean ATTRIBUTE_TOOLTIP_OVERHAUl = true;
    public static boolean ADVANCED_TOOLTIP = true;
    public static boolean DAMAGE_INDICATOR = true;
    public static boolean DAMAGE_INDICATOR_VANILLA_FONT = true;
    public static boolean DAMAGE_INDICATOR_COLOR_FORMAT = true;
    public static boolean DAMAGE_INDICATOR_TEXT_BOLD = true;
    public static boolean SCREEN_SHAKE = true;
    public static boolean MUSIC_BOSS = true;
    public static boolean REDUCE_DEBUG = true;
    public static boolean ENABLE_MOB_LEVELS = true;
    public static int SCREEN_SHAKE_AMOUNT = 100;
    public static GameHuds GAME_HUD = GameHuds.MOD;
    //SERVER
    public static boolean ENCHANTMENT_BREAK_LEVEL = false;
    public static AnvilCostSettings ANVIL_COSTING = AnvilCostSettings.REMOVE_REPAIR_SCALING;
    public static int ANVIL_COST_CAP = -1;
    public static double ANVIL_BREAK_CHANCES = 12.0D;

    public static void bake(ModConfig config) {
        try {
            BeyondHorizon.LOGGER.info("Syncing all configs here!");
            //---------------------------------------------------------------------//
            ATTRIBUTE_TOOLTIP_OVERHAUl = ModClientConfig.ATTRIBUTE_TOOLTIP_OVERHAUl.get();
            ADVANCED_TOOLTIP = ModClientConfig.ADVANCED_TOOLTIP.get();
            SCREEN_SHAKE = ModClientConfig.SCREEN_SHAKE.get();
            SCREEN_SHAKE_AMOUNT = ModClientConfig.SCREEN_SHAKE_AMOUNT.get();
            GAME_HUD = ModClientConfig.GAME_HUD.get();
            DAMAGE_INDICATOR = ModClientConfig.DAMAGE_INDICATOR.get();
            DAMAGE_INDICATOR_VANILLA_FONT = ModClientConfig.DAMAGE_INDICATOR_USE_VANILLA_FONT.get();
            DAMAGE_INDICATOR_COLOR_FORMAT = ModClientConfig.DAMAGE_INDICATOR_COLOR_FORMAT.get();
            DAMAGE_INDICATOR_TEXT_BOLD = ModClientConfig.DAMAGE_INDICATOR_TEXT_BOLD.get();
            MUSIC_BOSS = ModClientConfig.MUSIC_BOSS.get();
            REDUCE_DEBUG = ModClientConfig.REDUCE_DEBUG.get();
            ACCESSORY_BUTTON_X = ModClientConfig.ACCESSORY_BUTTON_X.get();
            ACCESSORY_BUTTON_Y = ModClientConfig.ACCESSORY_BUTTON_Y.get();
            //---------------------------------------------------------------------//
            MOBS_MIN_LEVEL_CAP = ModCommonConfig.MOBS_MAX_LEVEL_CAP.get();
            MOBS_MAX_LEVEL_CAP = ModCommonConfig.MOBS_MAX_LEVEL_CAP.get();
            ENABLE_MOB_LEVELS = ModCommonConfig.ENABLE_MOB_LEVELS.get();
            ENCHANTMENT_BREAK_LEVEL = ModCommonConfig.ENCHANTMENT_BREAK_LEVEL.get();
            ANVIL_COSTING = ModCommonConfig.ANVIL_COSTING.get();
            ANVIL_BREAK_CHANCES = ModCommonConfig.ANVIL_BREAK_CHANCES.get();
            ANVIL_COST_CAP = ModCommonConfig.ANVIL_COST_CAP.get();

        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("An exception was caused trying to load the config");
            e.printStackTrace();
        }
    }
}
