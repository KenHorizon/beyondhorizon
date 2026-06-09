package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.server.block.entity.BHBlockEntityBase;
import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.init.BHBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundBlockEntityDataPacket {
    private BlockPos blockPos;

    public ServerboundBlockEntityDataPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public ServerboundBlockEntityDataPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }
            BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
            if (blockEntity instanceof BHBlockEntityBase<?> be) {
                be.load(be.serializeNBT());
            }
        });
        context.setPacketHandled(true);
    }
}
