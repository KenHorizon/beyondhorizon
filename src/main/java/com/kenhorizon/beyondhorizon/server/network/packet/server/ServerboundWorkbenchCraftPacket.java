package com.kenhorizon.beyondhorizon.server.network.packet.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class ServerboundWorkbenchCraftPacket {
    private final ResourceLocation recipedId;
    public ServerboundWorkbenchCraftPacket(ResourceLocation recipedId) {
        this.recipedId = recipedId;
    }

    public ServerboundWorkbenchCraftPacket(FriendlyByteBuf buf) {
        this.recipedId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.recipedId);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level level = player.level();
            Optional<WorkbenchRecipe> recipes = level.getRecipeManager().byKey(this.recipedId).filter(r -> r instanceof WorkbenchRecipe).map(r -> (WorkbenchRecipe) r);
            recipes.ifPresent(recipe -> this.craft(player, recipe));
        });
        context.setPacketHandled(true);
    }
    private boolean hasEnoughItems(ServerPlayer player, Ingredient ingredient, int needed) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (ingredient.test(slot)) {
                count += slot.getCount();
                if (count >= needed) return true;
            }
        }
        return false;
    }
    private void craft(ServerPlayer player, WorkbenchRecipe recipe) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ing = recipe.getIngredients().get(i);
            int need = recipe.getCounts().get(i);
            if (!this.hasEnoughItems(player, ing, need)) {
                return;
            }
        }
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Item item = recipe.getIngredients().get(i).getItems()[0].getItem();
            int need = recipe.getCounts().get(i);
            player.getInventory().clearOrCountMatchingItems(itemStacks -> itemStacks.is(item), need, player.inventoryMenu.getCraftSlots());
        }
        BeyondHorizon.loggers().debug("Succcessfully crafted item from workbench...");
        player.addItem(recipe.getResultItem(null).copy());
    }
}
