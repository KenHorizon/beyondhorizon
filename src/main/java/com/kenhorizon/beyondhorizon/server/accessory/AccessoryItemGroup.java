package com.kenhorizon.beyondhorizon.server.accessory;

import net.minecraftforge.common.IExtensibleEnum;

public enum AccessoryItemGroup implements IExtensibleEnum {
    NONE,
    INFORMATION,
    QUIVER,
    ARROW_HEAD,
    IRON_SKIN,
    HYDRA,
    IMMOLATE,
    MOMENTUM,
    PROTECTION,
    STRING_BOW,
    BAND,
    GUARDIAN,
    DRAGON_INHERIT,
    FATALITY,
    VITALITY,
    WINGS,
    BOOTS;

    public static AccessoryItemGroup create(String name) {
        throw new IllegalStateException("Enum not extended");
    }
}