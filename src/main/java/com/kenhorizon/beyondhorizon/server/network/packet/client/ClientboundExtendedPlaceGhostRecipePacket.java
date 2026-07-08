package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.IRecipeUpdateListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundExtendedPlaceGhostRecipePacket {
    private final int containerId;
    private final ResourceLocation recipe;
    public ClientboundExtendedPlaceGhostRecipePacket(int id, Recipe<?> recipe) {
        this.containerId = id;
        this.recipe = recipe.getId();
    }

    public ClientboundExtendedPlaceGhostRecipePacket(FriendlyByteBuf buf) {
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
            Minecraft mc = Minecraft.getInstance();
            Player player = context.getSender();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = BeyondHorizon.PROXY.clientPlayer();
            }
            if (player != null) {
                AbstractContainerMenu menu = player.containerMenu;
                if (menu.containerId == this.getContainerId()) {
                    player.level().getRecipeManager().byKey(this.recipe).ifPresent(recipe -> {
                        if (mc.screen instanceof IRecipeUpdateListener listener) {
                            listener.setupGhostRecipe(recipe, menu.slots);
                        }
                    });
                }
            }
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
