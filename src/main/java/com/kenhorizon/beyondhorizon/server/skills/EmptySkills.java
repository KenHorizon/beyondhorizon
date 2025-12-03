package com.kenhorizon.beyondhorizon.server.skills;

public class EmptySkills extends Skill {
    public EmptySkills() {
        this.format = Format.NORMAL;
        this.skillType = Type.PASSIVE;
        this.tooltipEnable = false;
        this.universal();
    }
}
