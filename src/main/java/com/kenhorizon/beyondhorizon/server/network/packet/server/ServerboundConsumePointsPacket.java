package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundConsumePointsPacket {
    private final int index;
    private final int amount;
    public ServerboundConsumePointsPacket(int index, int amount) {
        this.index = index;
        this.amount = amount;
    }

    public ServerboundConsumePointsPacket(FriendlyByteBuf buf) {
        this.index = buf.readInt();
        this.amount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
        buf.writeInt(this.amount);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player sender = context.getSender();
            if (sender != null) {
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    sender = BeyondHorizon.PROXY.clientPlayer();
                }
                Level level = sender.level();
                Entity entity = level.getEntity(this.index);
                if (entity instanceof ServerPlayer player) {
                    if (player.totalExperience < 0) {
                        player.setExperiencePoints(0);
                    }
                    player.giveExperiencePoints(-this.amount);
                    RoleClass role = CapabilityCaller.roleClass((Player) player);
                    role.addExpPoints(this.amount);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
