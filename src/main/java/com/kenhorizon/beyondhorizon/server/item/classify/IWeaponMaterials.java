package com.kenhorizon.beyondhorizon.server.item.classify;

import net.minecraft.world.item.Tier;

public interface IWeaponMaterials extends Tier {
    String getName();

    boolean fireImmune();
}
