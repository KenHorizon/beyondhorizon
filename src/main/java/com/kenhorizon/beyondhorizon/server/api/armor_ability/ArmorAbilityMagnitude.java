package com.kenhorizon.beyondhorizon.server.api.armor_ability;

import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;

import java.util.Optional;

public class ArmorAbilityMagnitude extends ArmorAbility implements IAttack, IEntityProperties {

    private float magnitude;
    private float level;

    public ArmorAbilityMagnitude(float magnitude, float level) {
        this.magnitude = magnitude;
        this.level = level;
    }
    public ArmorAbilityMagnitude(float magnitude) {
        this(magnitude, 1);

    }

    public ArmorAbilityMagnitude() {
        this(0.0F, 1);

    }

    public float getLevel() {
        return level;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    @Override
    public Optional<IAttack> attack() {
        return Optional.of(this);
    }

    @Override
    public Optional<IEntityProperties> entityProperties() {
        return Optional.of(this);
    }
}
