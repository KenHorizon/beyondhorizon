package com.kenhorizon.beyondhorizon.server.world.network.packet.client;

import com.kenhorizon.beyondhorizon.server.world.network.ClientPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundLevelSystemPacket {
    private final int entityId;
    private final CompoundTag nbt;
    public ClientboundLevelSystemPacket(int entityId, CompoundTag nbt) {
        this.entityId = entityId;
        this.nbt = nbt;
    }

    public ClientboundLevelSystemPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.nbt = buf.readNbt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleLevelSystem(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public int getEntityId() {
        return entityId;
    }

    public CompoundTag getNbt() {
        return nbt;
    }
}