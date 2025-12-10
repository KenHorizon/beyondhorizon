package com.kenhorizon.beyondhorizon.server.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundGrabbedItemPacket {
    private final ItemStack itemStack;

    public ServerboundGrabbedItemPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ServerboundGrabbedItemPacket(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(this.itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LocalPlayer clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer != null) {
                clientPlayer.containerMenu.setCarried(this.itemStack);
            }
        });
        context.setPacketHandled(true);
    }
}
