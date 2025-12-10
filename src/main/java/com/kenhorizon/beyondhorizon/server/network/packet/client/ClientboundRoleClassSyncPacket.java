package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.client.entity.player.PlayerDataHandler;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundRoleClassSyncPacket {
    private final CompoundTag nbt;
    public ClientboundRoleClassSyncPacket(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public ClientboundRoleClassSyncPacket(FriendlyByteBuf buf) {
        this.nbt = buf.readNbt();
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