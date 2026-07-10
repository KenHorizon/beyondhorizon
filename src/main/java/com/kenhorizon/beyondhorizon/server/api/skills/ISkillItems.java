package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;

public interface ISkillItems {

    ISkill defaultInstance = () -> ItemStack.EMPTY;

    boolean hasSkill(Skill skill);

    Skill getFirstSkill(Skill skill);

    List<Skill> getSkillOf(Skill skill);

    int skillPresent();

    Collection<Skill> getSkills();

    Skill getActionSkils();

    default boolean hasCapability(ItemStack stack) {
        return true;
    }

    default int getSize() {
        return defaultInstance.getSize();
    }

}