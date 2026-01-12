package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ArmorSet {

    boolean matches(Player player);

    void applyBonus(Player player);

    void removeBonus(Player player);

    int countPieces(Player player);

    ResourceLocation getId();

    int tooltipLines();

    ArmorBonusSet getInstance();
}
