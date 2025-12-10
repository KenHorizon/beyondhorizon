package com.kenhorizon.beyondhorizon.server.item.debug_items;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.classes.RoleClassTypes;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugRoleClassResetItems extends BasicItem {

    public DebugRoleClassResetItems(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            RoleClass role = CapabilityCaller.roleClass(player);
            role.setRoles(RoleClassTypes.NONE);
            role.resetEverything();
        }
        return super.use(level, player, hand);
    }
}
