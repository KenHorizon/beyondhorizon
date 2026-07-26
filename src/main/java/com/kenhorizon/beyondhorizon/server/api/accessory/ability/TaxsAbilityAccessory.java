package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class TaxsAbilityAccessory extends AccessoryPassiveSkill {

    @Override
    public void onEntityKilled(DamageSource source, LivingEntity attacker, LivingEntity target) {
        var randoms = attacker.getRandom();
        Vec3 vec3 = Vec3.atLowerCornerWithOffset(target.getOnPos(), 0.15D, 1.01D, 0.15D).offsetRandom(randoms, 0.25F);
        ItemEntity itemEntity = new ItemEntity(attacker.level(), vec3.x(), vec3.y(), vec3.z(), new ItemStack(Items.EMERALD));
        itemEntity.setDefaultPickUpDelay();
        attacker.level().addFreshEntity(itemEntity);
    }
}
