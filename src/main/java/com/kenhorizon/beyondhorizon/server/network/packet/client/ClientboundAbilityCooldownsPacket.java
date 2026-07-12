package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import com.kenhorizon.libs.server.world.CooldownInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class ClientboundAbilityCooldownsPacket {
    private final Map<String, CooldownInstance> map;

    public ClientboundAbilityCooldownsPacket(Map<String, CooldownInstance> map) {
        this.map = map;
    }

    public static String readID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static CooldownInstance readCoolDownInstance(FriendlyByteBuf buffer) {
        int cooldown = buffer.readInt();
        int cooldownRemaining = buffer.readInt();
        return new CooldownInstance(cooldown, cooldownRemaining);
    }

    public static void writeId(FriendlyByteBuf buf, String id) {
        buf.writeUtf(id);
    }

    public static void writeCoolDownInstance(FriendlyByteBuf buf, CooldownInstance cooldownInstance) {
        buf.writeInt(cooldownInstance.getCooldown());
        buf.writeInt(cooldownInstance.getCooldownRemaining());
    }

    public ClientboundAbilityCooldownsPacket(FriendlyByteBuf buf) {
        this.map = buf.readMap(ClientboundAbilityCooldownsPacket::readID, ClientboundAbilityCooldownsPacket::readCoolDownInstance);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.map, ClientboundAbilityCooldownsPacket::writeId, ClientboundAbilityCooldownsPacket::writeCoolDownInstance);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleAbilityCooldowns(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public Map<String, CooldownInstance> getMap() {
        return map;
    }
}
