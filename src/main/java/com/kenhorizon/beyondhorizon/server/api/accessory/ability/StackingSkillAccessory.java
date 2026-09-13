package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfoTypes;

public abstract class StackingSkillAccessory extends AccessoryPassiveSkill implements IStackIconOverlay {
    private final StackableTags stackableTags;
    public StackingSkillAccessory(StackableTags stackableTags) {
        this.stackableTags = stackableTags;
    }

    public StackableTags getStackableTags() {
        return stackableTags;
    }

    public DamageInfoTypes damageType() {
        return DamageInfoTypes.PHYSICAL_DAMAGE;
    }

    @Override
    public StackableTags getStacks() {
        return this.getStackableTags();
    }
}
