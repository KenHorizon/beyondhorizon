package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.guis.LevelSystemScreen;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class ServerBoundOpenLevelSystemPacket {

    public ServerBoundOpenLevelSystemPacket() {
    }

    public ServerBoundOpenLevelSystemPacket(FriendlyByteBuf buf) {
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
