package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.IRecipeUpdateListener;
import com.kenhorizon.beyondhorizon.server.inventory.ExtendedRecipeBookMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundExtendedPlaceRecipePacket {
    private final int containerId;
    private final ResourceLocation recipe;
    private final boolean shiftDown;
    public ServerboundExtendedPlaceRecipePacket(int id, Recipe<?> recipe, boolean shiftDown) {
        this.containerId = id;
        this.recipe = recipe.getId();
        this.shiftDown = shiftDown;
    }

    public ServerboundExtendedPlaceRecipePacket(FriendlyByteBuf buf) {
        this.containerId = buf.readByte();
        this.recipe = buf.readResourceLocation();
        this.shiftDown = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeResourceLocation(this.recipe);
        buf.writeBoolean(this.shiftDown);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                AbstractContainerMenu menu = player.containerMenu;
                player.resetLastActionTime();
                if (!player.isSpectator() && menu.containerId == this.getContainerId() && player.containerMenu instanceof ExtendedRecipeBookMenu) {
                    if (!player.containerMenu.stillValid(player)) {
                        BeyondHorizon.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
                    } else {
                        player.level().getRecipeManager().byKey(this.recipe).ifPresent(recipe -> {
                            ((ExtendedRecipeBookMenu) player.containerMenu).handlePlacement(this.isShiftDown(), recipe, player);
                        });
                    }
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

    public boolean isShiftDown() {
        return shiftDown;
    }
}
