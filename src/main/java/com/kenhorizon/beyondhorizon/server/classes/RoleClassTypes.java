package com.kenhorizon.beyondhorizon.server.classes;

import com.kenhorizon.beyondhorizon.server.Utils;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum RoleClassTypes implements StringRepresentable {
    NONE,
    ASSSASSIN,
    MARKSMAN,
    CASTER,
    STRIKER,
    VANGAURD,
    SUPPORT;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String getName() {
        return Utils.capitalize(this.name().toLowerCase(Locale.ROOT));
    }
}
