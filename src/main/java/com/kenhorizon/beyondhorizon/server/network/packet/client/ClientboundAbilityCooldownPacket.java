package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundAbilityCooldownPacket {
    private final String id;
    private final int duration;

    public ClientboundAbilityCooldownPacket(String id, int duraiton) {
        this.id = id;
        this.duration = duraiton;
    }
    public ClientboundAbilityCooldownPacket(FriendlyByteBuf buf) {
        this.id  = buf.readUtf();
        this.duration = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeInt(this.duration);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleAbilityCooldown(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public String getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }
}