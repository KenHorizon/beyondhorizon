package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class StackingSkillAccessory extends AccessoryPassiveSkill implements IStackIconOverlay {
    private final StackableTags stackableTags;
    public StackingSkillAccessory(StackableTags stackableTags) {
        this.stackableTags = stackableTags;
    }

    public StackableTags getStackableTags() {
        return stackableTags;
    }

    public DamageType damageType() {
        return DamageType.PHYSICAL_DAMAGE;
    }

    @Override
    public StackableTags getStacks() {
        return this.getStackableTags();
    }
}
