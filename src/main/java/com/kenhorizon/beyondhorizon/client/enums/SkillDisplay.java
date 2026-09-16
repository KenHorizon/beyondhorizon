package com.kenhorizon.beyondhorizon.client.enums;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.OptionEnum;

import java.util.function.IntFunction;

public enum SkillDisplay implements OptionEnum {
    DISABLE(0, "Disable"),
    TEXT(1, "Text"),
    ICON(2, "Icon");

    private static final IntFunction<SkillDisplay> BY_ID = ByIdMap.continuous(SkillDisplay::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String key;

    private SkillDisplay(int id, String key) {
        this.id = id;
        this.key = key;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public String toString() {
        String type;
        switch (this) {
            case DISABLE:
                type = "disable";
                break;
            case TEXT:
                type = "text";
                break;
            case ICON:
                type = "icon";
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return type;
    }

    public static SkillDisplay byId(int id) {
        return BY_ID.apply(id);
    }
}
