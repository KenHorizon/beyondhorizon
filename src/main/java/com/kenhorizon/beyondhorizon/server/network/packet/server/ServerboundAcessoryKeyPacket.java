package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryEvent;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItems;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
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

import java.util.Optional;
import java.util.function.Supplier;

public class ServerboundAcessoryKeyPacket {
    private final ItemStack itemStack;
    private final int slots;
    private final int id;
    public ServerboundAcessoryKeyPacket(int id, ItemStack itemStack, int slots) {
        this.id = id;
        this.slots = slots;
        this.itemStack = itemStack;
    }

    public ServerboundAcessoryKeyPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.slots = buf.readInt();
        this.itemStack = buf.readItem();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.slots);
        buf.writeItem(this.itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player playerSided = context.getSender();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = BeyondHorizon.PROXY.clientPlayer();
            }
            if (playerSided != null) {
                Entity entity = playerSided.level().getEntity(this.id);
                if (entity instanceof Player player) {
                    IAccessoryItemHandler handler = Capabilities.accessory(player);
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack itemStack = handler.getStackInSlot(i);
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> caller) {
                            for (Accessory accessory : caller.getAccessories()) {
                                Optional<IAccessoryEvent> optional = accessory.IAccessory();
                                optional.ifPresent(callback -> callback.onKeypress(player, itemStack, this.slots));
                            }
                        }
                    }
                }
            }

        });
        context.setPacketHandled(true);
    }
}