package com.kenhorizon.beyondhorizon.server.capability;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkill;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

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
