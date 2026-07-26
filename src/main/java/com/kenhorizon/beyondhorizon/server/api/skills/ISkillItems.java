package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ISkillItems {

    boolean hasSkill(Skill skill);

    Skill getFirstSkill(Skill skill);

    List<Skill> getSkillOf(Skill skill);

    Collection<Skill> getSkills();

    Collection<Optional<Skill>> getActiveSkills();

    default Optional<Skill> getActiveSkill(ItemStack itemStack) {
        return this.getActiveSkills().isEmpty() ? Optional.empty() : this.getActiveSkills().stream().toList().get(this.getSkillSlot(itemStack));
    }

    default int getSkillSlot(ItemStack itemStack) {
        int index = itemStack.getCapability(BHCapabilties.SKILL_SLOTS).resolve().orElseThrow(NullPointerException::new).getSelectedSlot();
        index = Mth.clamp(index, 0, this.getActiveSkills().size() - 1);
        return index;
    }

    default boolean hasCapability(ItemStack stack) {
        return true;
    }
}