package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class AttributeOnlyAccessory extends Accessory implements IAccessoryEvent {
    public AttributeOnlyAccessory() {
        this.setTooltipEnableName(false);
    }
    @Override
    public Optional<IAccessoryEvent> accessory() {
        return Optional.of(this);
    }
}
