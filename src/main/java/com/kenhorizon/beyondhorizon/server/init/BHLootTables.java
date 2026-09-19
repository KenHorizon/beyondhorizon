package com.kenhorizon.beyondhorizon.server.init;

import com.google.common.collect.Sets;
import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class BHLootTables {
    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);

    public static final ResourceLocation SEALED_RUNIC_CRYPT_LEFT = register("chests/sealed_runic_crypt_left");
    public static final ResourceLocation SEALED_RUNIC_CRYPT_RIGHT = register("chests/sealed_runic_crypt_right");
    public static final ResourceLocation SEALED_RUNIC_CRYPT_MIDDLE = register("chests/sealed_runic_crypt_middle");

    public static final ResourceLocation COMMON_EQUIPMENTS = register("chests/common_equipments");

    private static ResourceLocation register(String id) {
        return register(BeyondHorizon.resource(id));
    }

    private static ResourceLocation register(ResourceLocation id) {
        if (LOCATIONS.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }
}
