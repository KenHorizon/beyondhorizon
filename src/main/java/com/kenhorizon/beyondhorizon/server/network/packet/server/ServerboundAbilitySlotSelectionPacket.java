package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAbilitySlotSelectionPacket {
    private final int selectedSlots;
    private final ItemStack stack;

    public ServerboundAbilitySlotSelectionPacket(ItemStack itemStack, int selectedSlots) {
        this.selectedSlots =selectedSlots;
        this.stack = itemStack;
    }

    public ServerboundAbilitySlotSelectionPacket(FriendlyByteBuf buf) {
        this.selectedSlots = buf.readInt();
        this.stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.selectedSlots);
        buf.writeItem(this.stack);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player playerSided = context.getSender();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = BeyondHorizon.PROXY.clientPlayer();
            }
            if (playerSided != null) {
                this.stack.getCapability(BHCapabilties.SKILL_SLOTS).ifPresent(handler -> {
                    handler.select(this.selectedSlots);
                });
            }

        });
        context.setPacketHandled(true);
    }
}
