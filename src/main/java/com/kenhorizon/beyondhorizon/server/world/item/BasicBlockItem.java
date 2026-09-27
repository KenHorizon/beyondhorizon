package com.kenhorizon.beyondhorizon.server.world.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BasicBlockItem extends BlockItem {
    public BasicBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    public BasicBlockItem(Supplier<? extends Block> block, Properties properties) {
        super(block.get(), properties);
    }
//    @Override
//    public void appendHoverText(ItemStack getStacks, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
//        Tooltips.addTooltipBlockLabel(getStacks, BHBlocks.MONOBLOCK, tooltip);
//    }
}
