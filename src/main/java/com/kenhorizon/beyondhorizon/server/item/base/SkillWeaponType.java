package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.skills.Skill;

import java.util.function.Predicate;

public enum SkillWeaponType {
    MELEE(Skill::isMeleeAbility),
    RANGED(Skill::isRangedAbility),
    THROWING(Skill::isThrowingAbility),
    UNIVERSAL(Skill::isUniversal);

    private Predicate<Skill> filter;

    SkillWeaponType(Predicate<Skill> filter) {
        this.filter = filter;
    }

    public Predicate<Skill> getFilter() {
        return filter;
    }
}