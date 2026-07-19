package com.kenhorizon.beyondhorizon.server.item.tooltips;

import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public class SkillTooltip implements TooltipComponent {
    private final ISkillItems items;

    public SkillTooltip(ISkillItems skills) {
        this.items = skills;
    }

    public ISkillItems getSkills() {
        return this.items;
    }
}