package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerDataHelper;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundManaSyncPacket {
    private final double mana;

    public ClientboundManaSyncPacket(double mana) {
//        BeyondHorizon.LOGGER.info("Network message: Receive Mana: {}",mana);
        this.mana = mana;
        //
    }
    public ClientboundManaSyncPacket(FriendlyByteBuf buf) {
        this.mana = buf.readDouble();
//        BeyondHorizon.LOGGER.info("Network message: Encode Receive Mana: {}", this.mana);
        //
    }

    public void toBytes(FriendlyByteBuf buf) {
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