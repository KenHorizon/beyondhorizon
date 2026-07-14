package com.kenhorizon.beyondhorizon.server.network;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.network.packet.client.*;
import com.kenhorizon.beyondhorizon.server.network.packet.server.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
        net.registerMessage(id(), ClientboundInventoryPacket.class, ClientboundInventoryPacket::write, ClientboundInventoryPacket::new, ClientboundInventoryPacket::handle);
        net.registerMessage(id(), ClientboundAccessoryInventoryPacket.class, ClientboundAccessoryInventoryPacket::write, ClientboundAccessoryInventoryPacket::new, ClientboundAccessoryInventoryPacket::handle);
        net.registerMessage(id(), ClientboundAccessoryPacket.class, ClientboundAccessoryPacket::write, ClientboundAccessoryPacket::new, ClientboundAccessoryPacket::handle);
        net.registerMessage(id(), ClientboundManaSyncPacket.class, ClientboundManaSyncPacket::write, ClientboundManaSyncPacket::new, ClientboundManaSyncPacket::handle);
        net.registerMessage(id(), ClientboundSetEntityChainedLinkPacket.class, ClientboundSetEntityChainedLinkPacket::write, ClientboundSetEntityChainedLinkPacket::new, ClientboundSetEntityChainedLinkPacket::handle);
        net.registerMessage(id(), ClientboundAbilityCooldownPacket.class, ClientboundAbilityCooldownPacket::write, ClientboundAbilityCooldownPacket::new, ClientboundAbilityCooldownPacket::handle);
        net.registerMessage(id(), ClientboundExtendedPlacedRecipePacket.class, ClientboundExtendedPlacedRecipePacket::write, ClientboundExtendedPlacedRecipePacket::new, ClientboundExtendedPlacedRecipePacket::handle);
        net.registerMessage(id(), ClientboundLevelSystemPacket.class, ClientboundLevelSystemPacket::write, ClientboundLevelSystemPacket::new, ClientboundLevelSystemPacket::handle);
        net.registerMessage(id(), ClientboundPlayerDataPacket.class, ClientboundPlayerDataPacket::write, ClientboundPlayerDataPacket::new, ClientboundPlayerDataPacket::handle);
        net.registerMessage(id(), ClientboundAddHealingOrbPacket.class, ClientboundAddHealingOrbPacket::write, ClientboundAddHealingOrbPacket::new, ClientboundAddHealingOrbPacket::handle);

        net.registerMessage(id(), ServerboundExtendedPlaceRecipePacket.class, ServerboundExtendedPlaceRecipePacket::write, ServerboundExtendedPlaceRecipePacket::new, ServerboundExtendedPlaceRecipePacket::handle);
        net.registerMessage(id(), ServerboundWorkbenchCraftPacket.class, ServerboundWorkbenchCraftPacket::write, ServerboundWorkbenchCraftPacket::new, ServerboundWorkbenchCraftPacket::handle);
        net.registerMessage(id(), ServerboundAccessoryInventoryPacket.class, ServerboundAccessoryInventoryPacket::write, ServerboundAccessoryInventoryPacket::new, ServerboundAccessoryInventoryPacket::handle);
        net.registerMessage(id(), ServerboundGrabbedItemPacket.class, ServerboundGrabbedItemPacket::write, ServerboundGrabbedItemPacket::new, ServerboundGrabbedItemPacket::handle);
        net.registerMessage(id(), ServerboundOpenLevelSystemPacket.class, ServerboundOpenLevelSystemPacket::write, ServerboundOpenLevelSystemPacket::new, ServerboundOpenLevelSystemPacket::handle);
        net.registerMessage(id(), ServerboundConsumePointsPacket.class, ServerboundConsumePointsPacket::write, ServerboundConsumePointsPacket::new, ServerboundConsumePointsPacket::handle);
        net.registerMessage(id(), ServerboundSkillPointsPacket.class, ServerboundSkillPointsPacket::write, ServerboundSkillPointsPacket::new, ServerboundSkillPointsPacket::handle);
        net.registerMessage(id(), ServerboundBossbarPacket.class, ServerboundBossbarPacket::write, ServerboundBossbarPacket::new, ServerboundBossbarPacket::handle);
        net.registerMessage(id(), ServerboundAbilityEffectPacket.class, ServerboundAbilityEffectPacket::write, ServerboundAbilityEffectPacket::new, ServerboundAbilityEffectPacket::handle);
        net.registerMessage(id(), ServerboundPlayerSwingArmPacket.class, ServerboundPlayerSwingArmPacket::write, ServerboundPlayerSwingArmPacket::new, ServerboundPlayerSwingArmPacket::handle);
        net.registerMessage(id(), ServerboundAcessoryKeyPacket.class, ServerboundAcessoryKeyPacket::write, ServerboundAcessoryKeyPacket::new, ServerboundAcessoryKeyPacket::handle);
        net.registerMessage(id(), ServerboundQuiverSelectedArrowPacket.class, ServerboundQuiverSelectedArrowPacket::write, ServerboundQuiverSelectedArrowPacket::new, ServerboundQuiverSelectedArrowPacket::handle);
        net.registerMessage(id(), ServerboundAbilitySlotSelectionPacket.class, ServerboundAbilitySlotSelectionPacket::write, ServerboundAbilitySlotSelectionPacket::new, ServerboundAbilitySlotSelectionPacket::handle);

    }



    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendAll(MSG msg) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(msg, player);
        }
    }

    public static <MSG> void sendAll(MSG msg, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }

    public static <MSG> void sendToClient(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}