package com.kenhorizon.beyondhorizon.server.api.level;

import net.minecraft.util.Mth;

public interface IAbilityInfo {

    int getCastTime();

    void setCastTime(int castTime);

    int getMaxCastTime();

    void setMaxCastTime(int maxCastTime);

    int getCooldown();

    void setCooldown(int cd);

    int getManaCost();

    void setManaCost(int manaCost);

    default float getCastTimeFactor() {
        return Mth.clamp((1.0F - ((float) this.getCastTime() / this.getMaxCastTime())), 0.0F, 1.0F);
    }

}
