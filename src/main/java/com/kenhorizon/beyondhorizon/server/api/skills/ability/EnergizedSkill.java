package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EnergizedSkill extends WeaponPassiveSkills {

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext damageDealt) {
        var stackTags = Capabilities.stackable(attacker);
        if (stackTags != null) {
            var sTag = stackTags.makeInstance(StackableTagInstance.ENERGIZE);
            sTag.add(6);
        }
    }
}

