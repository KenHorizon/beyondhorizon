package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.api.skills.ISkill;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import net.minecraft.world.item.ItemStack;

public class ItemizedSkillsCap implements ISkill {
    private final ItemStack itemStack;
    private final ISkillItems skillItems;

    public ItemizedSkillsCap(ISkillItems skillItems, ItemStack itemStack) {
        this.skillItems = skillItems;
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getStack() {
        return this.itemStack;
    }
}
