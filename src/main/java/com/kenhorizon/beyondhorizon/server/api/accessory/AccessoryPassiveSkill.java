package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class AccessoryPassiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {

    public AccessoryPassiveSkill(float magnitude, int level) {
        super(ItemAbilityType.PASSIVE, magnitude, level);
    }

    public AccessoryPassiveSkill() {
        super(ItemAbilityType.PASSIVE, 0, 1);
    }

    public AccessoryPassiveSkill(float magnitude) {
        super(ItemAbilityType.PASSIVE, magnitude, 1);
    }

    @Override
    public Optional<IEntityProperties> entityProperties() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> attack() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAccessoryEvent> accessory() {
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
