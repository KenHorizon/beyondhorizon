package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.damagesource.DamageSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnHitEffectHandler {
    static Multimap<DamageSource, Float> ONHIT_EFFECTS = HashMultimap.create();

    public static void add(DamageSource source, float onHitEffects) {
        ONHIT_EFFECTS.put(source, onHitEffects);
    }

    public static Multimap<DamageSource, Float> allOnHitEffects() {
        return ONHIT_EFFECTS;
    }

    public static void clear() {
        ONHIT_EFFECTS.clear();
    }
}
