package com.kenhorizon.beyondhorizon.configs.common;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.configs.BHConfigBuilder;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.configs.Configs;
import com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch.AnvilCostSettings;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModCommonConfig {
    public static ForgeConfigSpec.DoubleValue BLAZING_INFERNO_HP_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue BLAZING_INFERNO_DAMAGE_MULTIPLIER;

    public static ForgeConfigSpec.BooleanValue ENABLE_MOB_LEVELS;
    public static ForgeConfigSpec.BooleanValue CHANGE_DAMAGE_CALCULATION;
    public static ForgeConfigSpec.BooleanValue ENCHANTMENT_BREAK_LEVEL;
    public static ForgeConfigSpec.EnumValue<AnvilCostSettings> ANVIL_COSTING;
    public static ForgeConfigSpec.IntValue ANVIL_COST_CAP;
    public static ForgeConfigSpec.DoubleValue ANVIL_BREAK_CHANCES;


    public static final ForgeConfigSpec SPEC;
    public static final ModCommonConfig INSTANCE;


    static {
        Pair<ModCommonConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ModCommonConfig::new);
        SPEC = common.getRight();
        INSTANCE = common.getLeft();
    }

    public ModCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Beyond Horizon | Bosses");
        builder.push("Blazing Inferno");
        BLAZING_INFERNO_HP_MULTIPLIER = builder
                .worldRestart()
                .defineInRange("Blazing Inferno Health Multiplier", 1.0D, 0.0D, 1000000.0D);
        BLAZING_INFERNO_DAMAGE_MULTIPLIER = builder
                .worldRestart()
                .defineInRange("Blazing Inferno Damage Multiplier", 1.0D, 0.0D, 1000000.0D);
        builder.pop();
        builder.pop();

        builder.push("Beyond Horizon | Common Configs");
        builder.push("Anvil Patch");
        ENABLE_MOB_LEVELS = builder
                .worldRestart()
                .comment("Mob that spawn will have certain level ranging from 1-100 levels")
                .comment("Each level increase thier health and damage by 5%")
                .define("Spawn Mob with levels", false);
        CHANGE_DAMAGE_CALCULATION = builder
                .worldRestart()
                .comment("Replace vanilla damage calcuation into new damage calculation")
                .define("Change Damage Calculation", false);
        ANVIL_COSTING = builder
                .worldRestart()
                .comment("Change how anvil repair cost work")
                .comment("[Keep]")
                .comment("Keep the vanilla aspect mechanics")
                .comment("[Remove Repair Scaling]")
                .comment("Fixed at 1 XP Cost required when repairing item")
                .comment("[Enchantment Only]")
                .comment("Apply only increasing XP Cost at merging/adding enchantment")
                .comment("[Remove]")
                .comment("Remove totally the increasing exp cost when not applicable in repairing items")
                .defineEnum("Anvil Cost Settings", AnvilCostSettings.KEEP, AnvilCostSettings.REMOVE, AnvilCostSettings.KEEP, AnvilCostSettings.REMOVE_REPAIR_SCALING, AnvilCostSettings.ENCHANTMENT_ONLY);

        ANVIL_COST_CAP = builder
                .worldRestart()
                .comment("Change how much anvil xp cost capped works")
                .comment("Note:")
                .comment("Setting into -1 cause the anvil removed xp cost capped")
                .defineInRange("Anvil Xp Cost Limit", -1, -1, Integer.MAX_VALUE);
        ANVIL_BREAK_CHANCES = builder
                .worldRestart()
                .comment("Allow to set chances of anvil will break per use")
                .defineInRange("Anvil Break Chance", 12.0D, 0.0D, 100.0D);
        ENCHANTMENT_BREAK_LEVEL = builder
                .worldRestart()
                .comment("Vanilla behavior, when merging two items where the one on the right has an enchantment with a level beyond its max")
                .comment("(for example power 8) is to reset the level to the max (so the resulting item would have power 5)")
                .comment("This setting disables that")
                .comment("It does not, however, allow creating enchantments beyond the cap")
                .comment("So, for example, merging two power V books would still result in power V")
                .define("Enchantment Break Level", false);
        builder.pop();
        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, SPEC);
    }
}
