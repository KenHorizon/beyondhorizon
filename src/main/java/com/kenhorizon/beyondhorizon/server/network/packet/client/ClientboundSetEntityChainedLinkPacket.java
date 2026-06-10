package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.api.block.INodeBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSetEntityChainedLinkPacket {
    private final int sourceId;
    private final int destId;

    public ClientboundSetEntityChainedLinkPacket(int source, int dest) {
        this.sourceId = source;
        this.destId = dest;
    }
    public ClientboundSetEntityChainedLinkPacket(FriendlyByteBuf buf) {
        this.sourceId = buf.readInt();
        this.destId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.sourceId);
        buf.writeInt(this.destId);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            Level level = sender.level();
            Entity entity = level.getEntity(this.sourceId);
            if (entity instanceof INodeBlock node) {
                node.setLink(this.destId);
            }
        });
        context.setPacketHandled(true);
    }
}
