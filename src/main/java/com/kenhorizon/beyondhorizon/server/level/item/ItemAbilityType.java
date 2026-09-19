package com.kenhorizon.beyondhorizon.server.level.item;

import com.kenhorizon.beyondhorizon.server.Utils;
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
        return Utils.capitalize(this.name().toLowerCase(Locale.ROOT));
    }
}
