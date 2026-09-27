package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.world.entity.MobCategory;

public class BHMobCategory {
    public static MobCategory BOSS = MobCategory.create("BOSS", BeyondHorizon.ID, -1, false, true, 128);
    public static MobCategory MINIBOSS = MobCategory.create("MINIBOSS", BeyondHorizon.ID, -1, false, true, 128);
}
