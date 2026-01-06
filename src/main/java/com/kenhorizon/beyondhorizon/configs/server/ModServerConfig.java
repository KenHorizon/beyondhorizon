package com.kenhorizon.beyondhorizon.configs.server;

import com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch.AnvilCostSettings;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class ModServerConfig {


    public static ForgeConfigSpec.BooleanValue ENCHANTMENT_BREAK_LEVEL;
    public static ForgeConfigSpec.EnumValue<AnvilCostSettings> ANVIL_COSTING;
    public static ForgeConfigSpec.IntValue ANVIL_COST_CAP;
    public static ForgeConfigSpec.DoubleValue ANVIL_BREAK_CHANCES;


    public static final ForgeConfigSpec SPEC;
    public static final ModServerConfig INSTANCE;


    static {
        Pair<ModServerConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(ModServerConfig::new);
        SPEC = common.getRight();
        INSTANCE = common.getLeft();
    }

    public ModServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Beyond Horizon: Server Configs");
        ANVIL_COSTING = builder
                .comment("Change Anvil Repair Cost Work")
                .comment("  [Remove Repair Scaling] Fixed at 1 exp requiered when repairing item")
                .comment("  [Enchantment Only] Apply only Increasing cost exp to mering/adding enchantment")
                .comment("  [Keep] Keep the vanilla's mechanic")
                .comment("  [Remove] Remove totally the increasing exp cost when not applicable in repairing items")
                .defineEnum("Anvil Cost Settings", AnvilCostSettings.REMOVE_REPAIR_SCALING, AnvilCostSettings.KEEP, AnvilCostSettings.REMOVE, AnvilCostSettings.REMOVE_REPAIR_SCALING, AnvilCostSettings.ENCHANTMENT_ONLY);
        ANVIL_COST_CAP = builder
                .comment("Change the limit of the Anvil Level Cap is")
                .comment("Note:")
                .comment("Seting into -1 Remove the Level Cap")
                .defineInRange("Level Cap", -1, -1, 100);
        ANVIL_BREAK_CHANCES = builder
                .comment("Allow to set chances of anvil will break per use")
                .defineInRange("Anvil Break Chance", 0.12F, 0.0F, 1.0F);
        ENCHANTMENT_BREAK_LEVEL = builder
                .comment("Vanilla behavior, when merging two items where the one on the right has an enchantment with a level beyond its max")
                .comment("      (for example power 8) is to reset the level to the max (so the resulting item would have power 5)")
                .comment("      This setting disables that.")
                .comment("      It does not, however, allow creating enchantments beyond the cap")
                .comment("      So, for example, merging two power V books would still result in power V")
                .define("Enchantment Break Level", false);
        builder.pop();
    }

    public static void reset() {
    }

    public static void register(final ModLoadingContext modLoadingContext) {
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
}
