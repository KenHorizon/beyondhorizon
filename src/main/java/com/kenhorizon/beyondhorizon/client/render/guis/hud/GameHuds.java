package com.kenhorizon.beyondhorizon.client.render.guis.hud;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.OptionEnum;

import java.util.function.IntFunction;

public enum GameHuds implements OptionEnum {
    VANILLA(0, "Vanilla"),
    MOD(1, "Mod");

    private static final IntFunction<GameHuds> BY_ID = ByIdMap.continuous(GameHuds::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String key;

    private GameHuds(int id, String key) {
        this.id = id;
        this.key = key;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public String toString() {
        String type;
        switch (this) {
            case VANILLA:
                type = "default";
                break;
            case MOD:
                type = "mod";
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        return type;
    }

    public static GameHuds byId(int id) {
        return BY_ID.apply(id);
    }
}
