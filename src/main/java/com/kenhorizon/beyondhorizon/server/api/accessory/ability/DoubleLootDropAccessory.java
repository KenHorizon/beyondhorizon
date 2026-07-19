package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public class DoubleLootDropAccessory extends AccessoryPassiveSkill {

    public DoubleLootDropAccessory(float chances, int level) {
        super(chances, level);
    }

    public DoubleLootDropAccessory(float chances) {
        super(chances, 1);
    }

    @Override
    public Collection<ItemEntity> modifyLootdrops(LivingEntity target, Player player, Collection<ItemEntity> itemDrops) {
        if (target == null || player == null) return List.of();
        if (player.getRandom().nextFloat() * 100.0F <= ((this.getMagnitude() * 100.0F) * this.getLevel())) {
            return itemDrops;
        }
        return List.of();
    }
}
