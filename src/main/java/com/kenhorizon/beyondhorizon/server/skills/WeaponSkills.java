package com.kenhorizon.beyondhorizon.server.skills;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;

import java.util.Optional;

public class WeaponSkills extends Skill implements IAttack, IItemGeneric {
    private float magnitude;
    private float level;

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.of(this);
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getLevel() {
        return level;
    }
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }
    public void setLevel(float level) {
        this.level = level;
    }
}
