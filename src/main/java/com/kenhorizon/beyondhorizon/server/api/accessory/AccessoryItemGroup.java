package com.kenhorizon.beyondhorizon.server.api.accessory;

import net.minecraftforge.common.IExtensibleEnum;

public enum AccessoryItemGroup implements IExtensibleEnum {
    NONE,
    UNIQUE,
    STARTER,
    IMMOLATE,
    BLIGHT,
    INFORMATION,
    HERO_SWORD,
    SPELL_BLADE,
    POWER_GEM,
    STRING_BOW,
    FATALITY,
    VITALITY,
    ELIXIR,
    BOOTS;

    public static AccessoryItemGroup create(String name) {
        throw new IllegalStateException("Enum not extended");
    }
}