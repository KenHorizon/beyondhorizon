package com.kenhorizon.beyondhorizon.server.inventory.provider;

import com.kenhorizon.beyondhorizon.server.inventory.AccessoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;

public class AccessoryContainerProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.crafting");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new AccessoryMenu(containerId, inventory);
    }
}