package com.kenhorizon.beyondhorizon.server.item.materials;

import com.kenhorizon.beyondhorizon.server.skills.Skill;
import net.minecraft.world.item.Tier;

import java.util.List;

public interface IWeaponMaterials extends Tier {
    String getName();

    boolean hasSkills();

    boolean hasSkills(Skill abilityTrait);

    boolean fireImmune();

    List<Skill> getSkills();
}
