package com.kenhorizon.beyondhorizon.configs.client;

import com.kenhorizon.beyondhorizon.client.render.guis.hud.GameHuds;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModClientConfig {
    public static ForgeConfigSpec.BooleanValue REDUCE_DEBUG;
    public static ForgeConfigSpec.BooleanValue MUSIC_BOSS;
    public static ForgeConfigSpec.BooleanValue DAMAGE_INDICATOR;
    public static ForgeConfigSpec.BooleanValue DAMAGE_INDICATOR_USE_VANILLA_FONT;
    public static ForgeConfigSpec.BooleanValue DAMAGE_INDICATOR_TEXT_BOLD;
    public static ForgeConfigSpec.BooleanValue DAMAGE_INDICATOR_COLOR_FORMAT;
    public static ForgeConfigSpec.BooleanValue ATTRIBUTE_TOOLTIP_OVERHAUl;
    public static ForgeConfigSpec.BooleanValue ADVANCED_TOOLTIP;
    public static ForgeConfigSpec.BooleanValue SCREEN_SHAKE;
    public static ForgeConfigSpec.BooleanValue ALWAYS_SHOW_SKILL_ABILITY;
    public static ForgeConfigSpec.IntValue SCREEN_SHAKE_AMOUNT;
    public static ForgeConfigSpec.IntValue ACCESSORY_BUTTON_X;
    public static ForgeConfigSpec.IntValue ACCESSORY_BUTTON_Y;
    public static ForgeConfigSpec.EnumValue<GameHuds> GAME_HUD;

    public static final ForgeConfigSpec SPEC;
    public static final ModClientConfig INSTANCE;

    static {
        Pair<ModClientConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ModClientConfig::new);
        SPEC = common.getRight();
        INSTANCE = common.getLeft();
    }

    public ModClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Beyond Horizon | Client Configs");
        ALWAYS_SHOW_SKILL_ABILITY = builder
                .comment("Always show the skill ability's description")
                .define("Always Show Skill Ability", true);
        REDUCE_DEBUG = builder
                .comment("Reduce the Debug infos by removing some of it.")
                .define("Reduce Debug Infos", true);
        MUSIC_BOSS = builder
                .comment("Toggle the boss theme music when the boss spawn or active")
                .define("Music", true);
        DAMAGE_INDICATOR = builder
                .comment("Toggle to see the damage/heal dealt or take")
                .define("Damage Indicator", true);
        DAMAGE_INDICATOR_USE_VANILLA_FONT = builder
                .comment("Toggle to render the damage indicator number into vanilla style font")
                .define("Damage Indicator Vanilla Font", false);
        DAMAGE_INDICATOR_TEXT_BOLD = builder
                .comment("Toggle to see if the Damage indicator's number will render bold style")
                .define("Damage Indicator Text Bold", true);
        DAMAGE_INDICATOR_COLOR_FORMAT = builder
                .comment("Toggle to change how damage indicator color rendered depends what type of damage is being hit")
                .comment("If this disable the color is always rendered Gold")
                .comment("Physical Damage -> Gold")
                .comment("Magic Damage -> Blue")
                .comment("True Damage -> White")
                .comment("Raw Damage -> Red")
                .define("Damage Indicator Color Format", true);
        SCREEN_SHAKE = builder
                .comment("Allow to do screen shake effect")
                .define("Screen Shake", true);
        SCREEN_SHAKE_AMOUNT = builder
                .comment("Change how much Sceen Shake Effectiveness")
                .comment("Screen Shake Effectivness [1-100%]")
                .defineInRange("Screen Shake Multiplier", 100, 0, 100);
        GAME_HUD = builder
                .comment("Change how your game's hud will be displayed")
                .comment("  [Mod]")
                .comment("  - Removed Vanilla Armor and Health Bar Instead Display Icons and their Values")
                .comment("  [Vanilla]")
                .comment("  - Vanilla Game Experience")
                .defineEnum("In-Game Hud", GameHuds.MOD, GameHuds.VANILLA, GameHuds.MOD);
        ACCESSORY_BUTTON_X = builder
                .comment("X Position of Accessory Button in Accessory Screen")
                .defineInRange("Accessory Button Position X", 58, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        ACCESSORY_BUTTON_Y = builder
                .comment("Y Position of Accessory Button in Accessory Screen")
                .defineInRange("Accessory Button Position Y", 8, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        builder.pop();
        builder.push("Beyond Horizon | Tooltip Configs");
        ATTRIBUTE_TOOLTIP_OVERHAUl = builder
                .comment("Replace all tooltips with new re-visual")
                .comment("Changes:")
                .comment("All attribute based on percentages are now visually formated on percent instead of 0.1")
                .comment("Replacing modified and addative attribute into dark green color format")
                .define("Attribute Tooltip Overhaul", true);
        ADVANCED_TOOLTIP = builder
                .comment(" Extend item's tooltip information")
                .define("Show Advanced Tooltip", true);
        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, SPEC);
    }
}
