package com.kenhorizon.beyondhorizon.server.level.damagesource;

public interface IDamageSource {

    default boolean isTrueDamage() {
        return false;
    }
}
