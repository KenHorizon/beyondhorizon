package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.LevelSystemScreen;
import com.kenhorizon.beyondhorizon.server.item.ILeftClick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundPlayerSwingArmPacket {
    public ServerboundPlayerSwingArmPacket() {
    }

    public ServerboundPlayerSwingArmPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            if (player != null) {
                ItemStack leftItem = player.getItemInHand(InteractionHand.OFF_HAND);
                ItemStack rightItem = player.getItemInHand(InteractionHand.MAIN_HAND);
                if(leftItem.getItem() instanceof ILeftClick){
                    ((ILeftClick)leftItem.getItem()).onLeftClick(leftItem, player);
                }
                if(rightItem.getItem() instanceof ILeftClick){
                    ((ILeftClick)rightItem.getItem()).onLeftClick(rightItem, player);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
