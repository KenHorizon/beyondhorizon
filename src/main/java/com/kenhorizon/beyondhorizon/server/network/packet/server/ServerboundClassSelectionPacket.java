package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundClassSelectionPacket {
    private final int index;
    private final ResourceLocation roleClassTypes;
    public ServerboundClassSelectionPacket(int index, RoleClass roleClassTypes) {
        this.index = index;
        this.roleClassTypes = roleClassTypes.getResourceLocation();
    }

    public ServerboundClassSelectionPacket(FriendlyByteBuf buf) {
        this.index = buf.readInt();
        this.roleClassTypes = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
        buf.writeResourceLocation(this.roleClassTypes);
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
                    role.setRoles(BHRegistries.ROLE_CLASS_KEY.get().getValue(this.roleClassTypes));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
