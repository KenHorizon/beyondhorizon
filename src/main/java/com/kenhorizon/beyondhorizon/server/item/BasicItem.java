package com.kenhorizon.beyondhorizon.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicItem extends Item {
    public BasicItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

    }


    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }
}
