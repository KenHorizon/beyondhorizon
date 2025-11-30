package com.kenhorizon.beyondhorizon.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.ToolTipUI;
import java.util.List;

public class BasicBlockItem extends BlockItem {
    public BasicBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

//    @Override
//    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
//        Tooltips.addTooltipBlockLabel(itemStack, BHBlocks.MONOBLOCK, tooltip);
//    }
}
