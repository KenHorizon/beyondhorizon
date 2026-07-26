package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;

public class SlowAbilityAccessory extends AccessoryPassiveSkill {
    public enum Type {
        ONHIT,
        ABILITY;
    }

    private Type type = Type.ONHIT;
    public SlowAbilityAccessory(float slow, Type type) {
        this.type = type;
        this.setMagnitude(slow);
    }
}
