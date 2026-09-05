package com.kenhorizon.beyondhorizon.server.item.tooltips;

import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class SkillTooltip implements TooltipComponent {
    private final ISkillItems items;

    public SkillTooltip(ISkillItems skills) {
        this.items = skills;
    }

    public ISkillItems getSkills() {
        return this.items;
    }
}