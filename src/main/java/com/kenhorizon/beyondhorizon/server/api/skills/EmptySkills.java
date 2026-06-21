package com.kenhorizon.beyondhorizon.server.api.skills;

public class EmptySkills extends Skill {
    public EmptySkills() {
        super(Type.PASSIVE);
        this.tooltipEnable = false;
        this.universal();
    }
}
