package com.kenhorizon.beyondhorizon.client.entity.player;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundRoleClassSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDataHandler {
    private static final HashMap<Integer, PlayerData> playerData = new HashMap<>();
    public static PlayerData get(Player player) {
        if (player == null) return null;
        int key = isRemote(player);
        if (!playerData.containsKey(key)) {
            playerData.put(key, new PlayerData(player));
        }
        return playerData.get(key);
    }

    public static void clean() {
        List<Integer> removals = new ArrayList<>();
        for (Map.Entry<Integer, PlayerData> entry : playerData.entrySet()) {
            PlayerData playerData = entry.getValue();
            if (playerData != null && playerData.refPlayer.get() == null) {
                removals.add(entry.getKey());
            }
        }
        removals.forEach(playerData::remove);
    }

    private static int isRemote(Player player) {
        return player == null ? 0 : player.hashCode() << 1 + (player.level().isClientSide() ? 1 : 0);
    }
}
