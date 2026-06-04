package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import java.util.ArrayList;
import java.util.List;

public class StackableTagInstance {

    public static final StackableTags ENERGIZE = new StackableTags("energize", 100);

    private static List<StackableTags> TAGS = new ArrayList<>();

    public static void add(StackableTags stackableTags) {
        TAGS.add(stackableTags);
    }

    public static List<StackableTags> getTags() {
        return TAGS;
    }

    public static void registerAll() {
        TAGS.add(ENERGIZE);
    }
}
