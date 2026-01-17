package com.kenhorizon.beyondhorizon.server.api.skills.item_properties;

import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponSkills;

import java.util.Optional;

public class ItemPropertiesSkill extends WeaponSkills implements IItemProperties {

    public ItemPropertiesSkill() {
        this.disableTooltip();
        this.universal();
        this.setLevel(1);
        this.setMagnitude(1);
        this.type(Type.PASSIVE);
    }

    @Override
    public Optional<IItemProperties> itemProperties() {
        return Optional.of(this);
    }
}
