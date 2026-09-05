package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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

    default void addAbilityTooltip(ItemStack itemStack, List<Component> tooltip) {
        if (hasSkill(Skills.NONE.get())) return;
        int size = this.getSkills().size();
        int activePresent = this.getActiveSkills().size();
        if (activePresent > 1) {
            tooltip.add(Tooltips.numberMax(this.getSkillSlot(itemStack) + 1, activePresent));
        }
        for (int i = 0; i < this.getSkills().size(); i++) {
            Skill skill = this.getSkills().stream().toList().get(i);
            if (!skill.getAttributeModifiers().isEmpty()) {
                size--;
                skill.addTooltipAttributes(itemStack, tooltip);
            }
            if (skill.isPassive()) {
                skill.addTooltip(itemStack, tooltip, size, Utils.isShiftPressed(), i == 0);
            }
            if (this.getActiveSkill(itemStack).isPresent()) {
                if (skill.isActive() && skill == this.getActiveSkill(itemStack).get()) {
                    skill.addTooltip(itemStack, tooltip, size, Utils.isShiftPressed(), i == 0);
                }
            }
        }
        tooltip.add(CommonComponents.EMPTY);
    }

    default boolean hasCapability(ItemStack stack) {
        return true;
    }
}