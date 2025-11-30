package com.kenhorizon.beyondhorizon.server.util.attributes;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public interface IAttributeRegistryHelper<T> {
    T get(ResourceLocation id);

    ResourceLocation getId(T value);

    boolean isRegistered(T value);

    boolean exists(ResourceLocation id);

    Collection<T> getValues();
}
