package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.Skill;

public class EmptySkills extends Skill {
    public EmptySkills() {
        super(Type.PASSIVE);
        this.tooltipEnable = false;
        this.universal();
    }
}
