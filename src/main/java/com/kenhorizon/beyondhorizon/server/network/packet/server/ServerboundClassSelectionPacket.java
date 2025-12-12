package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.classes.RoleClassTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundClassSelectionPacket {
    private final int index;
    private final RoleClassTypes roleClassTypes;
    public ServerboundClassSelectionPacket(int index, RoleClassTypes roleClassTypes) {
        this.index = index;
        this.roleClassTypes = roleClassTypes;
    }

    public ServerboundClassSelectionPacket(FriendlyByteBuf buf) {
        this.index = buf.readInt();
        this.roleClassTypes = buf.readEnum(RoleClassTypes.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
        buf.writeEnum(this.roleClassTypes);
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
                if (entity instanceof Player player) {
                    RoleClass role = CapabilityCaller.roleClass((Player) player);
                    role.setRoles(this.roleClassTypes);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
