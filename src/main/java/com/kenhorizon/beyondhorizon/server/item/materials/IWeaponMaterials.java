package com.kenhorizon.beyondhorizon.server.item.materials;

import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import net.minecraft.world.item.Tier;

import java.util.List;

public interface IWeaponMaterials extends Tier {
    String getName();

    boolean fireImmune();
}
