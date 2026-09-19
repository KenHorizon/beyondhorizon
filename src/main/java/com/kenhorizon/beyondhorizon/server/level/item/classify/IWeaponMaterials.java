package com.kenhorizon.beyondhorizon.server.level.item.classify;

import net.minecraft.world.item.Tier;

public interface IWeaponMaterials extends Tier {
    String getName();

    boolean fireImmune();
}
