package com.kenhorizon.beyondhorizon.server.inventory.menu;

import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Blocks;

public class ForgeCraftingMenu extends CraftingMenu {
    public ForgeCraftingMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, inventory, access);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BHBlocks.FORGE.get());
    }
}
