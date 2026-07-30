package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundManaSyncPacket {
    private final double mana;

    public ClientboundManaSyncPacket(double mana) {
        this.mana = mana;
        //
    }
    public ClientboundManaSyncPacket(FriendlyByteBuf buf) {
        this.mana = buf.readDouble();
        //
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.mana);
        //
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = BeyondHorizon.PROXY.clientPlayer();
            if (player != null) {
                var data = Capabilities.data(player);
                data.setSyncMana(this.getMana());

            }
        });
        context.setPacketHandled(true);
    }

    public double getMana() {
        return mana;
    }
}