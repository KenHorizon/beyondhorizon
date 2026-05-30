package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AccessorySkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {

    public AccessorySkill(float magnitude, int level) {
        super(magnitude, level);
    }

    public AccessorySkill() {
        super(0, 1);
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
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.getMagnitude() > 0.0F && this.getLevel() > 0.0F) {
            return Component.translatable(this.createId(), MathUtils.format0(this.getMagnitude()), this.getLevel());
        } else {
            return Component.translatable(this.createId(), MathUtils.format0(this.getMagnitude()));
        }
    }
}
