package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.libs.server.world.CooldownInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class ClientboundAbilityCooldownPacket {
    private final Map<String, CooldownInstance> map;

    public ClientboundAbilityCooldownPacket(Map<String, CooldownInstance> map) {
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

    public ClientboundAbilityCooldownPacket(FriendlyByteBuf buf) {
        this.map = buf.readMap(ClientboundAbilityCooldownPacket::readID, ClientboundAbilityCooldownPacket::readCoolDownInstance);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.map, ClientboundAbilityCooldownPacket::writeId, ClientboundAbilityCooldownPacket::writeCoolDownInstance);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = BeyondHorizon.PROXY.clientPlayer();
            var skillCooldown = Capabilities.data(player);
            this.map.forEach((s, instance) -> {
                BeyondHorizon.LOGGER.debug("Client Skill Cooldown:[{}, {} {}, {}]", s, instance.getCooldown(), instance.getCooldownRemaining(), instance.getCooldownPercent());
                skillCooldown.addCooldown(s, instance.getCooldown(), instance.getCooldownRemaining());
            });
        });
        context.setPacketHandled(true);
    }
}
