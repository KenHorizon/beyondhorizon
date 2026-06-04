package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class EnergizedAccessory extends AccessorySkill {

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        var stackTags = Capabilities.stackable(entity);
        if (stackTags != null) {
            var sTag = stackTags.getInstance(StackableTagInstance.ENERGIZE);
            float stepProgress = entity.moveDist / entity.nextStep;
            if (!entity.level().isClientSide()) {
                if (entity.moveDist > entity.nextStep) {
                    BeyondHorizon.LOGGER.debug("[Energize] Adding energize stacks!");
                    sTag.add(1);
                }
            }
        }
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        var stackTags = Capabilities.stackable(attacker);
        if (stackTags != null) {
            var sTag = stackTags.getInstance(StackableTagInstance.ENERGIZE);
            sTag.add(6);
        }
    }
}
