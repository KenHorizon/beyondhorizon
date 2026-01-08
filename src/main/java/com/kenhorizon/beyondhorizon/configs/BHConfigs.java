package com.kenhorizon.beyondhorizon.configs;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.GameHuds;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.configs.common.ModCommonConfig;
import com.kenhorizon.beyondhorizon.configs.server.ModServerConfig;
import com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch.AnvilCostSettings;
import net.minecraftforge.fml.config.ModConfig;

public class BHConfigs {
    //CLIENT
    public static boolean ATTRIBUTE_TOOLTIP_OVERHAUl = true;
    public static boolean ADVANCED_TOOLTIP_ACCESSORY = true;
    public static boolean ADVANCED_TOOLTIP_SKILL = true;
    public static boolean ADVANCED_TOOLTIP = true;
    public static boolean SCREEN_SHAKE = true;
    public static int SCREEN_SHAKE_AMOUNT = 100;
    public static GameHuds GAME_HUD = GameHuds.MOD;
    //SERVER
    public static boolean ENCHANTMENT_BREAK_LEVEL = false;
    public static AnvilCostSettings ANVIL_COSTING = AnvilCostSettings.REMOVE_REPAIR_SCALING;
    public static int ANVIL_COST_CAP = -1;
    public static double ANVIL_BREAK_CHANCES = 12.0D;
    //BLAZING INFENRO
    public static double BLAZING_INFERNO_HP_MULTIPLIER = 1.0D;
    public static double BLAZING_INFERNO_DAMAGE_MULTIPLIER = 1.0D;

    public static void bake(ModConfig config) {
        try {
            BeyondHorizon.LOGGER.info("Syncing all configs here!");
            //---------------------------------------------------------------------//
            ATTRIBUTE_TOOLTIP_OVERHAUl = ModClientConfig.ATTRIBUTE_TOOLTIP_OVERHAUl.get();
            ADVANCED_TOOLTIP = ModClientConfig.ADVANCED_TOOLTIP.get();
            ADVANCED_TOOLTIP_ACCESSORY = ModClientConfig.ADVANCED_TOOLTIP_ACCESSORY.get();
            ADVANCED_TOOLTIP_SKILL = ModClientConfig.ADVANCED_TOOLTIP_SKILL.get();
            SCREEN_SHAKE = ModClientConfig.SCREEN_SHAKE.get();
            SCREEN_SHAKE_AMOUNT = ModClientConfig.SCREEN_SHAKE_AMOUNT.get();
            GAME_HUD = ModClientConfig.GAME_HUD.get();
            //---------------------------------------------------------------------//
            ENCHANTMENT_BREAK_LEVEL = ModCommonConfig.ENCHANTMENT_BREAK_LEVEL.get();
            ANVIL_COSTING = ModCommonConfig.ANVIL_COSTING.get();
            ANVIL_BREAK_CHANCES = ModCommonConfig.ANVIL_BREAK_CHANCES.get();
            ANVIL_COST_CAP = ModCommonConfig.ANVIL_COST_CAP.get();
            //---------------------------------------------------------------------//
            BLAZING_INFERNO_HP_MULTIPLIER = ModCommonConfig.BLAZING_INFERNO_HP_MULTIPLIER.get();
            BLAZING_INFERNO_DAMAGE_MULTIPLIER = ModCommonConfig.BLAZING_INFERNO_DAMAGE_MULTIPLIER.get();

        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("An exception was caused trying to load the config");
            e.printStackTrace();
        }
    }
}
