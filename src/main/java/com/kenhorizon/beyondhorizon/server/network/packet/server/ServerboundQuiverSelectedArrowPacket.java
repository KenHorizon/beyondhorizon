package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.inventory.QuiverMenu;
import com.kenhorizon.beyondhorizon.server.item.QuiverItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundQuiverSelectedArrowPacket {
    private final int index;
    public ServerboundQuiverSelectedArrowPacket(int index) {
        this.index = index;
    }

    public ServerboundQuiverSelectedArrowPacket(FriendlyByteBuf buf) {
        this.index = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            AbstractContainerMenu menu = context.getSender().containerMenu;
            if (menu instanceof QuiverMenu quiverMenu) {
                quiverMenu.getItemStack().getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                    if (handler instanceof QuiverItemStackHandler quiverItemStackHandler) {
                        quiverItemStackHandler.setSelectedSlot(this.index);
                    }
                });
            }
        });
    }
}
