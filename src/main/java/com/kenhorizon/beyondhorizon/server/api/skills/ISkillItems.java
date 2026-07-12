package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.ISkillSlots;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.world.item.ItemStack;

import javax.swing.text.html.Option;
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
        int index = itemStack.getCapability(BHCapabilties.SKILL_SLOTS).resolve().orElseThrow(NullPointerException::new).getSelectedSlot();
        return this.getActiveSkills().isEmpty() ? Optional.empty() : this.getActiveSkills().stream().toList().get(index - 1);
    }

    default boolean hasCapability(ItemStack stack) {
        return true;
    }
}