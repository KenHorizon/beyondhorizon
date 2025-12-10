package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAccessoryInventoryPacket {
    private final int slot;
    private final ItemStack itemStack;
    private final int id;
    public ServerboundAccessoryInventoryPacket(int slot, int id, ItemStack itemStack) {
        this.slot = slot;
        this.id = id;
        this.itemStack = itemStack;
    }

    public ServerboundAccessoryInventoryPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.id = buf.readInt();
        this.itemStack = buf.readItem();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
        buf.writeInt(this.id);
        buf.writeItem(this.itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player playerSided = context.getSender();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = BeyondHorizon.PROXY.clientPlayer();
            }
            if (playerSided != null) {
                Entity entity = playerSided.level().getEntity(this.id);
                if (entity instanceof Player player) {
                    player.getCapability(BHCapabilties.ACCESSORY).ifPresent(handler -> {
                        handler.setStackInSlot(this.slot, this.itemStack);
                    });
                }
            }

        });
        context.setPacketHandled(true);
    }
}
