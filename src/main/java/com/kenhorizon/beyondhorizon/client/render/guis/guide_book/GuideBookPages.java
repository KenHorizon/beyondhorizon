package com.kenhorizon.beyondhorizon.client.render.guis.guide_book;

import com.google.common.collect.ImmutableList;

import java.util.stream.IntStream;

public enum GuideBookPages {
    INTRODUCTION(0),
    DAMAGE_TYPES(0),
    EFFECT_TYPES(1),
    GAME_MECHANICS(1),
    ACCESSORY(1),
    LEVEL_SYSTEM(1),
    DIFFICULTY(1),
    STATS(2);

    public int pages;

    GuideBookPages(int pages) {
        this.pages = pages;
    }
}
