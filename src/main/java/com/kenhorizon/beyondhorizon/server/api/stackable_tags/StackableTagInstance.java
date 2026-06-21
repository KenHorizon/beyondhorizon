package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class StackableTagInstance {

    public static final StackableTags ENERGIZE = new StackableTags("energize", 100);
    public static final StackableTags SAINT_DEMON_CROWN_STACKS = new StackableTags("saint_demon_crown_stacks");

    private static final List<StackableTags> TAGS = new ArrayList<>();

    public static void add(StackableTags stackableTags) {
        TAGS.add(stackableTags);
    }

    public static List<StackableTags> getTags() {
        return TAGS;
    }

    public static void registerAll() {
        TAGS.add(ENERGIZE);
        TAGS.add(SAINT_DEMON_CROWN_STACKS);
    }
}
