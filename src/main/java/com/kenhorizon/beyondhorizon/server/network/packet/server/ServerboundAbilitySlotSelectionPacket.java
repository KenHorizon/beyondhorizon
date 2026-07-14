package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAbilitySlotSelectionPacket {
    private final int selectedSlots;

    public ServerboundAbilitySlotSelectionPacket(int selectedSlots) {
        this.selectedSlots = selectedSlots;
    }

    public ServerboundAbilitySlotSelectionPacket(FriendlyByteBuf buf) {
        this.selectedSlots = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.selectedSlots);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player playerSided = context.getSender();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = BeyondHorizon.PROXY.clientPlayer();
            }
            if (playerSided != null) {
                ItemStack stack = PlayerData.getHeldingItem(playerSided);
                stack.getCapability(BHCapabilties.SKILL_SLOTS).ifPresent(handler -> {
                    handler.select(this.selectedSlots);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
