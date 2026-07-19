package com.kenhorizon.beyondhorizon.server.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ManaDataEvent extends PlayerEvent {
    private double amount;

    public ManaDataEvent(Player player, double mana) {
        super(player);
        this.setAmount(mana);
    }

    public void setAmount(double mana) {
        this.amount = mana;
    }

    public double getAmount() {
        return amount;
    }
}
