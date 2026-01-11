package com.kenhorizon.beyondhorizon.server.item.materials;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ArmorItem;

import java.util.function.Supplier;

public interface IArmorBaseMaterial {
    ImmutableMultimap<Supplier<Attribute>, Double> getAttributes(ArmorItem.Type type);
}
