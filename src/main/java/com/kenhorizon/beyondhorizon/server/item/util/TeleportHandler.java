package com.kenhorizon.beyondhorizon.server.item.util;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class TeleportHandler {

    public static void teleportHome(ServerPlayer player, Level level) {
        ServerLevel serverLevel = player.server.getLevel(Level.OVERWORLD);
        if (serverLevel == null) serverLevel = (ServerLevel) level;
        LevelData data = serverLevel.getLevelData();
        teleport(player, data.getXSpawn(), data.getYSpawn(), data.getZSpawn());
    }

    public static void teleport(ServerPlayer player, double x, double y, double z) {
        EntityTeleportEvent event = ForgeEventFactory.onEnderTeleport(player, x, y, z);
        if (!event.isCanceled()) {
            player.teleportTo(x, y, z);
        }
    }
}
