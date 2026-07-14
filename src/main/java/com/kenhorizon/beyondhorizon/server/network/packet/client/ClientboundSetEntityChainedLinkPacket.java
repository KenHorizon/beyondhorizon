package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
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

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.sourceId);
        buf.writeInt(this.destId);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ClientPacketHandler.handleEntityChainedLink(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getDestId() {
        return destId;
    }
}
