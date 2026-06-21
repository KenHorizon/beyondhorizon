package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EnergizedSkill extends WeaponPassiveSkills {

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        var stackTags = Capabilities.stackable(attacker);
        if (stackTags != null) {
            var sTag = stackTags.getInstance(StackableTagInstance.ENERGIZE);
            sTag.add(6);
        }
    }
}

