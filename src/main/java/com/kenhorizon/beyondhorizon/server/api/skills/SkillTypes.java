package com.kenhorizon.beyondhorizon.server.api.skills;

import java.util.function.Predicate;

public enum SkillTypes {
    MELEE(Skill::isMeleeAbility),
    RANGED(Skill::isRangedAbility),
    THROWING(Skill::isThrowingAbility),
    UNIVERSAL(Skill::isUniversal);

    private Predicate<Skill> filter;

    SkillTypes(Predicate<Skill> filter) {
        this.filter = filter;
    }

    public Predicate<Skill> getFilter() {
        return filter;
    }
}