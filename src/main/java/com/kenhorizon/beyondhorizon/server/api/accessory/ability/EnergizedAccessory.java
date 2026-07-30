package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EnergizedAccessory extends AccessoryPassiveSkill implements IStackIconOverlay {

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        var stackTags = Capabilities.stackable(entity);
        if (stackTags != null) {
            var sTag = stackTags.makeInstance(StackableTagInstance.ENERGIZE);
            if (!entity.level().isClientSide() && sTag != null) {
                float steps = entity.moveDist / entity.nextStep;
                if (entity.moveDist > entity.nextStep) {
                    sTag.add(1);
                }
                if (steps % 0.24F == 0) {
                    sTag.add(1);
                }
            }
        }

    }

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        var stackTags = Capabilities.stackable(attacker);
        if (stackTags != null) {
            var sTag = stackTags.makeInstance(StackableTagInstance.ENERGIZE);
            sTag.add(6);
        }
    }
    @Override
    public StackableTags getStacks() {
        return StackableTagInstance.ENERGIZE;
    }
}
