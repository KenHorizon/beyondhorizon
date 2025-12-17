package com.kenhorizon.beyondhorizon.server.entity;

import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundBossbarPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BHBossInfo extends ServerBossEvent {
    protected final BHBaseEntity entity;
    private final Set<ServerPlayer> unseen = new HashSet<>();

    public BHBossInfo(BHBaseEntity entity) {
        super(entity.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossBarOverlay.PROGRESS);
        this.setVisible(entity.hasBossBar());
        this.entity = entity;
    }
    public void update() {
        this.setProgress(this.entity.getHealth() / this.entity.getMaxHealth());
        Iterator<ServerPlayer> iterator = this.unseen.iterator();
        while (iterator.hasNext()) {
            ServerPlayer player = iterator.next();
            if (this.entity.getSensing().hasLineOfSight(player)) {
                super.addPlayer(player);
                iterator.remove();
            }
        }
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        NetworkHandler.sendNonLocal(new ServerboundBossbarPacket(this.getId(), this.entity), player);
        if (this.entity.getSensing().hasLineOfSight(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        NetworkHandler.sendNonLocal(new ServerboundBossbarPacket(this.getId(), null), player);
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}