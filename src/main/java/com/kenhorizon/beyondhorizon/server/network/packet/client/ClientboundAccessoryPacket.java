package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundAccessoryPacket {
    private final CompoundTag nbt;
    public ClientboundAccessoryPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }
    public ClientboundAccessoryPacket(FriendlyByteBuf buf) {
        this.nbt = buf.readNbt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleAccessoryData(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public CompoundTag getNbt() {
        return nbt;
    }
}