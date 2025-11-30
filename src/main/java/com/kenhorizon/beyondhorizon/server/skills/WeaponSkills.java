package com.kenhorizon.beyondhorizon.server.skills;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;

import java.util.Optional;

public class WeaponSkills extends Skill implements IAttack, IItemGeneric {

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.of(this);
    }
}
