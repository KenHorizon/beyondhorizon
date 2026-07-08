package com.kenhorizon.beyondhorizon.server.api.entity.player;

import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

public final class PlayerDataHelper {
    public static LazyOptional<PlayerData> getPlayerData(Player player) {
        if (player != null) {
            return player.getCapability(BHCapabilties.PLAYER_DATA);
        } else {
            return LazyOptional.empty();
        }
    }
}
