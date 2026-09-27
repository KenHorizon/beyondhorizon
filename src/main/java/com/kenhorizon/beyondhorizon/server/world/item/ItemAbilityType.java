package com.kenhorizon.beyondhorizon.server.world.item;

import com.kenhorizon.beyondhorizon.server.util.Helpers;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ItemAbilityType implements StringRepresentable {
    NONE,
    PASSIVE,
    ACTIVE;

    public boolean isPassive() {
        return this != ItemAbilityType.ACTIVE;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String getName() {
        return Helpers.capitalize(this.name().toLowerCase(Locale.ROOT));
    }
}
