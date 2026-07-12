package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.server.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundExtendedPlacedRecipePacket {
    private final int containerId;
    private final ResourceLocation recipe;
    public ClientboundExtendedPlacedRecipePacket(int id, Recipe<?> recipe) {
        this.containerId = id;
        this.recipe = recipe.getId();
    }

    public ClientboundExtendedPlacedRecipePacket(FriendlyByteBuf buf) {
        this.containerId = buf.readByte();
        this.recipe = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeResourceLocation(this.recipe);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleExtendedPlacedRecipe(this, supplier);
        });
        context.setPacketHandled(true);
    }

    public int getContainerId() {
        return containerId;
    }

    public ResourceLocation getRecipe() {
        return recipe;
    }
}
