package com.kenhorizon.beyondhorizon.server.api.armor_ability;

import net.minecraft.world.entity.LivingEntity;

public interface IArmorAbility {

    boolean matches(LivingEntity entity);

    void applyBonus(LivingEntity entity);

    void removeBonus(LivingEntity entity);

    int countPieces(LivingEntity entity);

    ArmorAbility getInstance();
}
