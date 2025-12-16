package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientboundRoleClassSyncPacket(CompoundTag nbt) {

    public ClientboundRoleClassSyncPacket(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = BeyondHorizon.PROXY.clientPlayer();
            if (player != null) {
                player.getCapability(BHCapabilties.ROLE_CLASS).ifPresent(cap -> {
                    cap.loadNbt(this.nbt);
                });
            }
        });
        context.setPacketHandled(true);
    }
}