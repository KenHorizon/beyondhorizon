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
    public static boolean ATTRIBUTE_TOOLTIP_OVERHAUl;
    public static boolean ADVANCED_TOOLTIP_ACCESSORY;
    public static boolean ADVANCED_TOOLTIP_SKILL;
    public static boolean ADVANCED_TOOLTIP;
    public static boolean SCREEN_SHAKE;
    public static int SCREEN_SHAKE_AMOUNT;
    public static GameHuds GAME_HUD;
    //SERVER
    public static boolean ENCHANTMENT_BREAK_LEVEL;
    public static AnvilCostSettings ANVIL_COSTING;
    public static int ANVIL_COST_CAP;
    public static double ANVIL_BREAK_CHANCES;

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
            ENCHANTMENT_BREAK_LEVEL = ModServerConfig.ENCHANTMENT_BREAK_LEVEL.get();
            ANVIL_COSTING = ModServerConfig.ANVIL_COSTING.get();
            ANVIL_BREAK_CHANCES = ModServerConfig.ANVIL_BREAK_CHANCES.get();
            ANVIL_COST_CAP = ModServerConfig.ANVIL_COST_CAP.get();

        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("An exception was caused trying to load the config");
            e.printStackTrace();
        }
    }
}
