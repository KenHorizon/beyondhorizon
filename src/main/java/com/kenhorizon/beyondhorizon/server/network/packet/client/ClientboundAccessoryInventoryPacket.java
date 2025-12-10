package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.inventory.provider.AccessoryContainerProvider;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundGrabbedItemPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundAccessoryInventoryPacket {
    private final ItemStack itemStack;

    public ClientboundAccessoryInventoryPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ClientboundAccessoryInventoryPacket(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(this.itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                ItemStack itemStack = sender.isCreative() ? this.itemStack : sender.containerMenu.getCarried();
                sender.containerMenu.setCarried(ItemStack.EMPTY);
                sender.openMenu(new AccessoryContainerProvider());
                if (!itemStack.isEmpty()) {
                    sender.containerMenu.setCarried(itemStack);
                    NetworkHandler.sendToPlayer(new ServerboundGrabbedItemPacket(this.itemStack), sender);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
