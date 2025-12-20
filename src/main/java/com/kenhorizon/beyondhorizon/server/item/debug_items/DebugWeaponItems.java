package com.kenhorizon.beyondhorizon.server.item.debug_items;

import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DebugWeaponItems extends BasicItem {
    public DebugWeaponItems(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        target.hurt(target.level().damageSources().generic(), target.getMaxHealth());
        return super.hurtEnemy(itemStack, target, attacker);
    }
}
