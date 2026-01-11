package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface ArmorSet {

    boolean matches(Player player);

    void applyBonus(Player player);

    void removeBonus(Player player);

    int countPieces(Player player);

    ResourceLocation getId();
}
