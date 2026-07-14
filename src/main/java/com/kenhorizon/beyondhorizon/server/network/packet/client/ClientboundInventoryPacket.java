package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundInventoryPacket {
    private final ItemStack stacks;
    public ClientboundInventoryPacket(ItemStack itemStack) {
        this.stacks = itemStack;
    }
    public ClientboundInventoryPacket(FriendlyByteBuf buf) {
        this.stacks = buf.readItem();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stacks);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleMinecraftInventory(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public ItemStack getStacks() {
        return stacks;
    }
}
