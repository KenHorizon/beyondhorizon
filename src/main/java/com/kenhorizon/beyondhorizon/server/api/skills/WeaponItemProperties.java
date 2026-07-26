package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;

import java.util.Optional;

public class WeaponItemProperties extends WeaponPassiveSkills implements IItemProperties {

    public WeaponItemProperties() {
        this.disableTooltip();
        this.universal();
        this.setLevel(1);
        this.setMagnitude(1);
        this.type(ItemAbilityType.NONE);
    }

    @Override
    public Optional<IItemProperties> itemProperties() {
        return Optional.of(this);
    }
}
