package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class ServerboundAcessoryKeyPacket {
    private final ItemStack itemStack;
    private final int slots;
    private final int id;
    public ServerboundAcessoryKeyPacket(int id, ItemStack itemStack, int slots) {
        BeyondHorizon.LOGGER.debug("Sending key packets!!");
        this.id = id;
        this.slots = slots;
        this.itemStack = itemStack;
    }

    public ServerboundAcessoryKeyPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.slots = buf.readInt();
        this.itemStack = buf.readItem();

    }

    public void write(FriendlyByteBuf buf) {
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
                    AccessoryHelper.getInventory(player).ifPresent(handler -> {
                        var stacks = handler.getStacks();
                        for (int i = 0; i < handler.getSlots(); i++) {
                            if (i == this.slots) {
                                ItemStack itemStack = stacks.getStackInSlot(i);
                                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem caller) {
                                    for (Accessory accessory : caller.getAccessories()) {
                                        Optional<IAccessoryEvent> optional = accessory.accessory();
                                        optional.ifPresent(callback -> {
                                            callback.onKeybindPressed(player, itemStack, this.slots);
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }

        });
        context.setPacketHandled(true);
    }
}