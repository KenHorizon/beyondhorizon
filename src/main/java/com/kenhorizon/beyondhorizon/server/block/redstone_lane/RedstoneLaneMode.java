package com.kenhorizon.beyondhorizon.server.block.redstone_lane;

import net.minecraft.util.StringRepresentable;

public enum RedstoneLaneMode implements StringRepresentable {

    UNPOWERED("unpowered"),
    POWERED("powered");

    private final String name;

    private RedstoneLaneMode(String string) { this.name = string; }

    public String toString() { return this.name; }

    @Override
    public String getSerializedName() {
        return this.name;
    }

}
