package com.kenhorizon.beyondhorizon.server.block.the_forge;

import net.minecraft.util.StringRepresentable;

public enum ForgeTypeStation implements StringRepresentable {
    CRAFTING("crafting"),
    ANVIL("anvil"),
    SMELTING("smelting");

    private final String name;

    private ForgeTypeStation(String string) {
        this.name = string;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

}
