package com.kenhorizon.beyondhorizon.server.network;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.IRecipeUpdateListener;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.api.block.INodeBlock;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerDataHelper;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.inventory.provider.AccessoryContainerProvider;
import com.kenhorizon.beyondhorizon.server.network.packet.client.*;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundGrabbedItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleAccessoryData(ClientboundAccessoryPacket packet, Supplier<NetworkEvent.Context> context) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (player != null) {
            AccessoryHelper.getInventory(player).ifPresent(handler -> {
                handler.deserializeNBT(packet.nbt());
            });
        }
    }

    public static void handleAccessoryInventory(ClientboundAccessoryInventoryPacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer sender = context.get().getSender();
        if (sender != null) {
            ItemStack itemStack = sender.isCreative() ? packet.getStacks() : sender.containerMenu.getCarried();
            sender.containerMenu.setCarried(ItemStack.EMPTY);
            sender.openMenu(new AccessoryContainerProvider());
            if (!itemStack.isEmpty()) {
                sender.containerMenu.setCarried(itemStack);
                NetworkHandler.sendToPlayer(new ServerboundGrabbedItemPacket(packet.getStacks()), sender);
            }
        }
    }
    public static void handleMinecraftInventory(ClientboundInventoryPacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer sender = context.get().getSender();
        if (sender != null) {
            ItemStack itemStack = sender.isCreative() ? packet.getStacks() : sender.containerMenu.getCarried();
            sender.containerMenu.setCarried(ItemStack.EMPTY);
            sender.doCloseContainer();
            if (!itemStack.isEmpty()) {
                if (!sender.isCreative()) {
                    sender.containerMenu.setCarried(itemStack);
                }
                NetworkHandler.sendToPlayer(new ServerboundGrabbedItemPacket(packet.getStacks()), sender);
            }
        }
    }
    public static void handleManaData(ClientboundManaSyncPacket packet, Supplier<NetworkEvent.Context> context) {

    }
    public static void handlePlayerData(ClientboundPlayerDataPacket packet, Supplier<NetworkEvent.Context> context) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        PlayerDataHelper.getPlayerData(player).ifPresent(handler -> {
            handler.loadNbt(packet.getNbt());
        });
    }

    public static void handleAbilityCooldown(ClientboundAbilityCooldownPacket packet, Supplier<NetworkEvent.Context> context) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        var skillCooldown = Capabilities.data(player);
        packet.getMap().forEach((s, instance) -> {
            skillCooldown.addCooldown(s, instance.getCooldown());
        });
    }

    public static void handleAbilityCooldowns(ClientboundAbilityCooldownsPacket packet, Supplier<NetworkEvent.Context> context) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        var skillCooldown = Capabilities.data(player);
        packet.getMap().forEach((s, instance) -> {
            skillCooldown.addCooldown(s, instance.getCooldown(), instance.getCooldownRemaining());
        });
    }
    public static void handleLevelSystem(ClientboundLevelSystemPacket packet, Supplier<NetworkEvent.Context> context) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (player != null) {
            player.getCapability(BHCapabilties.ROLE_CLASS).ifPresent(cap -> {
                cap.loadNbt(packet.getNbt());
            });
        }
    }
    public static void handleEntityChainedLink(ClientboundSetEntityChainedLinkPacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer sender = context.get().getSender();
        Level level = sender.level();
        Entity entity = level.getEntity(packet.getSourceId());
        if (entity instanceof INodeBlock node) {
            node.setLink(packet.getDestId());
        }
    }
    public static void handleExtendedPlacedRecipe(ClientboundExtendedPlacedRecipePacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = context.get().getSender();
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            player = BeyondHorizon.PROXY.clientPlayer();
        }
        if (player != null) {
            AbstractContainerMenu menu = player.containerMenu;
            if (menu.containerId == packet.getContainerId()) {
                player.level().getRecipeManager().byKey(packet.getRecipe()).ifPresent(recipe -> {
                    if (mc.screen instanceof IRecipeUpdateListener listener) {
                        listener.setupGhostRecipe(recipe, menu.slots);
                    }
                });
            }
        }
    }
}
