package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAbilityEffectPacket {
    private int sourceID;
    private int targetID;

    public ServerboundAbilityEffectPacket(Entity source, Entity target) {
        if (source instanceof ILinkedEntity) {
            sourceID = source.getId();
            targetID = target.getId();
        }
    }

    public ServerboundAbilityEffectPacket(FriendlyByteBuf buf) {
        this.sourceID = buf.readVarInt();
        this.targetID = buf.readVarInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.sourceID);
        buf.writeVarInt(this.targetID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entitySource = Minecraft.getInstance().level.getEntity(this.sourceID);
                Entity entityTarget = Minecraft.getInstance().level.getEntity(this.targetID);
                if (entitySource instanceof ILinkedEntity && entityTarget != null) {
                    ((ILinkedEntity) entitySource).link(entityTarget);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
