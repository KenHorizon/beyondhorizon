package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;

public class EmptySkills extends Skill {
    public EmptySkills() {
        super(ItemAbilityType.PASSIVE);
        this.tooltipEnable = false;
        this.universal();
    }
}
