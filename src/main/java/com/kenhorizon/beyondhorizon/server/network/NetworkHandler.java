package com.kenhorizon.beyondhorizon.server.network;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.network.packet.client.*;
import com.kenhorizon.beyondhorizon.server.network.packet.server.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class NetworkHandler {
    public static SimpleChannel INSTANCE;
    private static final String PTC_VERSION = "1";

    private static int pocketID = 0;

    private static int id() {
        return pocketID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(BeyondHorizon.resource("main"))
                .networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals)
                .serverAcceptedVersions(PTC_VERSION::equals)
                .simpleChannel();

        INSTANCE = net;
        // BUILDER
        net.registerMessage(id(), ClientboundInventoryPacket.class, ClientboundInventoryPacket::toBytes, ClientboundInventoryPacket::new, ClientboundInventoryPacket::handle);
        net.registerMessage(id(), ClientboundAccessoryInventoryPacket.class, ClientboundAccessoryInventoryPacket::toBytes, ClientboundAccessoryInventoryPacket::new, ClientboundAccessoryInventoryPacket::handle);
        net.registerMessage(id(), ClientboundRoleClassSyncPacket.class, ClientboundRoleClassSyncPacket::toBytes, ClientboundRoleClassSyncPacket::new, ClientboundRoleClassSyncPacket::handle);
        net.registerMessage(id(), ClientboundPlayerDataSyncPacket.class, ClientboundPlayerDataSyncPacket::toBytes, ClientboundPlayerDataSyncPacket::new, ClientboundPlayerDataSyncPacket::handle);
        net.registerMessage(id(), ClientboundActiveSkillSyncPacket.class, ClientboundActiveSkillSyncPacket::toBytes, ClientboundActiveSkillSyncPacket::new, ClientboundActiveSkillSyncPacket::handle);

        net.registerMessage(id(), ServerboundWorkbenchCraftPacket.class, ServerboundWorkbenchCraftPacket::toBytes, ServerboundWorkbenchCraftPacket::new, ServerboundWorkbenchCraftPacket::handle);
        net.registerMessage(id(), ServerboundAccessoryInventoryPacket.class, ServerboundAccessoryInventoryPacket::toBytes, ServerboundAccessoryInventoryPacket::new, ServerboundAccessoryInventoryPacket::handle);
        net.registerMessage(id(), ServerboundGrabbedItemPacket.class, ServerboundGrabbedItemPacket::toBytes, ServerboundGrabbedItemPacket::new, ServerboundGrabbedItemPacket::handle);
        net.registerMessage(id(), ServerboundOpenLevelSystemPacket.class, ServerboundOpenLevelSystemPacket::toBytes, ServerboundOpenLevelSystemPacket::new, ServerboundOpenLevelSystemPacket::handle);
        net.registerMessage(id(), ServerboundConsumePointsPacket.class, ServerboundConsumePointsPacket::toBytes, ServerboundConsumePointsPacket::new, ServerboundConsumePointsPacket::handle);
        net.registerMessage(id(), ServerboundSkillPointsPacket.class, ServerboundSkillPointsPacket::toBytes, ServerboundSkillPointsPacket::new, ServerboundSkillPointsPacket::handle);
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendAll(MSG msg, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }

    public static <MSG> void sendToClient(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    public static <MSG> void sendToClientsInLevel(MSG message, ResourceKey<Level> levelResourceKey) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> levelResourceKey), message);
    }
}