package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.handler.UndyingTotemAbility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ImmuneDeathAccessory extends AccessoryPassiveSkill {

    @Override
    public boolean onEntityDeath(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.ETERNAL_LIFE.get()) {
            if (entity instanceof Player player && player.level() instanceof ServerLevel slevel) {
                return !(player.isCreative() || player.isSpectator()) && !UndyingTotemAbility.onUse(slevel, (ServerPlayer) player);
            }
        }
        return false;
    }
}
