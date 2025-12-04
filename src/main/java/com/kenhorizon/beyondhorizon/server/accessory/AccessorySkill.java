package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AccessorySkill extends Accessory implements IItemGeneric, IAttack {

    public AccessorySkill(float magnitude, int level) {
        super(magnitude, level);
    }

    public AccessorySkill() {
        super(0, 0);
    }

    @Override
    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.FEATHER_FEET.get()) {
            entity.fallDistance = -1;
        }
    }
}
