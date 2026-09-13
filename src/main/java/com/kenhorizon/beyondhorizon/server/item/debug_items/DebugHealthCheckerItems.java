package com.kenhorizon.beyondhorizon.server.item.debug_items;

import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.server.util.RaycastUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugHealthCheckerItems extends BasicItem {
    public DebugHealthCheckerItems(Properties properties) {
        super(properties);
    }


    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Using item in Living entity it display health in chat"));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Entity lookedAtEntity = RaycastUtil.getEntityLookedAt(player);
            if (lookedAtEntity instanceof LivingEntity target) {
                player.sendSystemMessage(target.getDisplayName().copy().append(Component.literal(  ":" + target.getHealth() + "/" + target.getMaxHealth())));
            }
        }
        return super.use(level, player, hand);
    }
}
