package com.kenhorizon.beyondhorizon.client.entity.player;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import net.minecraft.world.entity.player.Player;

import java.lang.ref.WeakReference;

public class PlayerData {
    public WeakReference<Player> refPlayer;
    private final boolean client;


    public PlayerData(Player player) {
        this.refPlayer = new WeakReference<>(player);
        this.client = player.level().isClientSide();
    }

    public Player player() {
        return this.refPlayer.get();
    }

    public RoleClass roles() {
        return CapabilityCaller.roleClass(player());
    }
}
