package com.kenhorizon.beyondhorizon.server.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;

import java.util.List;

public class HarvestBlockEvent extends BlockEvent.BreakEvent  {
    protected List<ItemStack> itemDrops;
    protected ItemStack itemStackUse;
    protected boolean canDropLoot;

    public HarvestBlockEvent(Level level, BlockPos pos, BlockState state, Player player, ItemStack itemStackUse, List<ItemStack> itemDrops) {
        super(level, pos, state, player);
        this.itemDrops = itemDrops;
        this.itemStackUse = itemStackUse;
    }

    public ItemStack getItemStackUse() {
        return itemStackUse;
    }

    public List<ItemStack> getItemDrops() {
        return itemDrops;
    }


    public void setCanDropLoot(boolean canDropLoot) {
        this.canDropLoot = canDropLoot;
    }

    public boolean isCanDropLoot() {
        return this.canDropLoot;
    }
}
