package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.client.ClientProxy;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerboundBossbarPacket {
    private UUID bossID;
    private boolean remove;
    private ResourceLocation registryName;
    private int renderType;
    public ServerboundBossbarPacket(UUID bossID, LivingEntity entity, int renderType) {
        this.bossID = bossID;
        this.renderType = renderType;
        if (entity != null) {
            this.registryName = RegistryHelper.getKeyOrThrow(entity.getType());
            this.remove = false;
        } else {
            this.registryName = null;
            this.remove = true;
        }
    }

    public ServerboundBossbarPacket(FriendlyByteBuf buf) {
        this.renderType = buf.readInt();
        this.bossID = buf.readUUID();
        this.remove = buf.readBoolean();
        if (!this.remove) this.registryName = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.renderType);
        buf.writeUUID(this.bossID);
        buf.writeBoolean(this.remove);
        if (!this.remove && this.registryName != null) buf.writeResourceLocation(this.registryName);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (this.registryName == null) {
                ClientProxy.BOSS_BAR_REGISTRY.remove(this.bossID);
            }
            else {
                ClientProxy.BOSS_BAR_REGISTRY.put(this.bossID, new BHBossInfo.BossBar(this.renderType, this.registryName));
            }
        });
        context.setPacketHandled(true);
    }
}
