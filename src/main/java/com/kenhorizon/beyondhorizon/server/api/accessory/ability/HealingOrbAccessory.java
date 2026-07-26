package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.entity.misc.HealingOrb;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class HealingOrbAccessory extends AccessoryPassiveSkill {

    private static final int ORB_SPAWN_COUNT = 5;

    @Override
    public void onEntityKilled(DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker.level() instanceof ServerLevel slvl) {
            HealingOrb.spawnOrbs(attacker, slvl, target.position(), ORB_SPAWN_COUNT, (entity) -> {
                entity.value = 1 + (int) EntityUtils.getMissingHealth(attacker, 0.05F);
            });
        }
    }
}
