package com.kenhorizon.beyondhorizon.configs;

import com.kenhorizon.beyondhorizon.configs.common.ModCommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BHConfigBuilder {

    protected ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, boolean requiredRestart, String name, boolean defaultValue, String comment) {
        return requiredRestart ? builder.worldRestart().comment(comment).define(name, defaultValue) : builder.comment(comment).define(name, defaultValue);
    }
    protected ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment) {
        return buildBoolean(builder, false, name, defaultValue, comment);
    }
    protected ForgeConfigSpec.IntValue buildNumerical(ForgeConfigSpec.Builder builder, boolean requiredRestart, String name, int value, int min, int max, String comment) {
        return requiredRestart ? builder.worldRestart().comment(comment).defineInRange(name, value, min, max) : builder.comment(comment).defineInRange(name, value, min, max);
    }
    protected ForgeConfigSpec.DoubleValue buildNumerical(ForgeConfigSpec.Builder builder, boolean requiredRestart, String name, double value, double min, double max, String comment) {
        return requiredRestart ? builder.worldRestart().comment(comment).defineInRange(name, value, min, max) : builder.comment(comment).defineInRange(name, value, min, max);
    }
    protected ForgeConfigSpec.IntValue buildNumerical(ForgeConfigSpec.Builder builder, String name, int value, int min, int max, String comment) {
        return buildNumerical(builder, false, name, value, min, max, comment);
    }
    protected ForgeConfigSpec.DoubleValue buildNumerical(ForgeConfigSpec.Builder builder, String name, double value, double min, double max, String comment) {
        return buildNumerical(builder, false, name, value, min, max, comment);
    }
    protected <T extends Enum<T>> ForgeConfigSpec.EnumValue<T> buildEnum(ForgeConfigSpec.Builder builder, boolean requiredRestart, String name, String comment, T defaultValue, T... enumValues) {
        return requiredRestart ? builder.worldRestart().comment(comment).defineEnum(name, defaultValue, enumValues) : builder.comment(comment).defineEnum(name, defaultValue, enumValues);
    }

}
