package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryEvent;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AccessoryPassiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {

    public AccessoryPassiveSkill(float magnitude, int level) {
        super(ItemAbilityType.PASSIVE, magnitude, level);
    }

    public AccessoryPassiveSkill() {
        super(ItemAbilityType.PASSIVE, 0, 1);
    }

    @Override
    public Optional<IEntityProperties> IEntityProperties() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.of(this);
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        if (this.getMagnitude() > 0.0F && this.getLevel() > 0.0F) {
            return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()), this.getLevel());
        } else {
            return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()));
        }
    }
}
