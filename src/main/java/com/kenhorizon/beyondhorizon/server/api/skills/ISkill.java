package com.kenhorizon.beyondhorizon.server.api.skills;

import net.minecraft.world.item.ItemStack;

public interface ISkill {

    ItemStack getStack();

    default int maxSkillSlots() {
        return 4;
    }


    default int getSize() {
        return 0;
    }

    default void setSkillMaxSlots(int slots) {

    }

    default void selectSlots(int slots) {

    }

    default Skill getSkill() {
        return null;
    }
}
