package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraftforge.common.IExtensibleEnum;

public enum AccessoryItemGroup implements IExtensibleEnum {
    NONE,
    UNIQUE,
    INFORMATION,
    QUIVER,
    MOMENTUM,
    PROTECTION,
    STRING_BOW,
    BAND,
    GUARDIAN,
    FATALITY,
    VITALITY,
    BOOTS;

    public static AccessoryItemGroup create(String name) {
        throw new IllegalStateException("Enum not extended");
    }
}