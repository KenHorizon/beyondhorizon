package com.kenhorizon.beyondhorizon.server.skills;

import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.List;

public interface ISkillItems<T extends Item> {

    public T getItem();

    boolean hasSkill(Skill skill);

    Skill getSkill(Skill skill);

    List<Skill> getAllSkillPresent(Skill skill);

    int skillPresent();

    Collection<Skill> getSkills();
}