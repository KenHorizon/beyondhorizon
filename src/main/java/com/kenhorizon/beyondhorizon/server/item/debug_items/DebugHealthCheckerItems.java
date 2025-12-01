package com.kenhorizon.beyondhorizon.server.item.debug_items;

import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DebugHealthCheckerItems extends BasicItem {
    public DebugHealthCheckerItems(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity target) {
            player.sendSystemMessage(Component.literal("Entity Health: " + target.getHealth() + "/" + target.getMaxHealth()));
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
