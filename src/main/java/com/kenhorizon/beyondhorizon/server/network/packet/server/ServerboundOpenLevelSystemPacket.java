package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.LevelSystemScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundOpenLevelSystemPacket {

    public ServerboundOpenLevelSystemPacket() {
    }

    public ServerboundOpenLevelSystemPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BeyondHorizon.PROXY.openScreen(new LevelSystemScreen());
        });
        context.setPacketHandled(true);
    }
}
