package com.kenhorizon.beyondhorizon.server.block.wire;

import net.minecraft.util.StringRepresentable;

public enum WireLaneMode implements StringRepresentable {

    UNPOWERED("unpowered"),
    POWERED("powered");

    private final String name;

    private WireLaneMode(String string) { this.name = string; }

    public String toString() { return this.name; }

    @Override
    public String getSerializedName() {
        return this.name;
    }

}
