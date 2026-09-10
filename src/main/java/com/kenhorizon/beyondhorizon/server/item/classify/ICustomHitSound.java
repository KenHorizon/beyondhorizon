package com.kenhorizon.beyondhorizon.server.item.classify;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ICustomHitSound {
    boolean hitSound(Level level, Player player, LivingEntity entity);
}
