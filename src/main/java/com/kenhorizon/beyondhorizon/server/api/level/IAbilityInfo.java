package com.kenhorizon.beyondhorizon.server.api.level;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public interface IAbilityInfo {

    int getCastTime();

    void setCastTime(int castTime);

    int getMaxCastTime();

    void setMaxCastTime(int maxCastTime);

    int getCooldown();

    void setCooldown(int cd);

    double getManaCost();

    void setManaCost(double manaCost);

    default float getCastTimeFactor(Player player) {
        float castTimeReduction = (float) AttributeUtils.getValue(player, BHAttributes.CAST_TIME.get());
        return Mth.clamp((1.0F - ((float) this.getCastTime() / (this.getMaxCastTime() * (1.0F - castTimeReduction)))), 0.0F, 1.0F);
    }

}
