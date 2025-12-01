package com.kenhorizon.beyondhorizon.server.skills;

import java.util.function.Predicate;

public enum SkillTypes {
    MELEE(Skill::isMeleeAbility),
    RANGED(Skill::isRangedAbility),
    THROWING(Skill::isThrowingAbility),
    ACCESSORY(Skill::isAccessory),
    UNIVERSAL(Skill::isUniversal);

    private Predicate<Skill> filter;

    SkillTypes(Predicate<Skill> filter) {
        this.filter = filter;
    }

    public Predicate<Skill> getFilter() {
        return filter;
    }
}