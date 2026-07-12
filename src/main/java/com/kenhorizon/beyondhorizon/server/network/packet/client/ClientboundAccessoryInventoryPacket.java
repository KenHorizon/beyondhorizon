package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundAccessoryInventoryPacket {
    private final ItemStack stacks;

    public ClientboundAccessoryInventoryPacket(ItemStack itemStack) {
        this.stacks = itemStack;
    }

    public ClientboundAccessoryInventoryPacket(FriendlyByteBuf buf) {
        this.stacks = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(this.stacks);
    }

    public ItemStack getStacks() {
        return stacks;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleAccessoryInventory(this, supplier);
        });
        context.setPacketHandled(true);
    }
}
