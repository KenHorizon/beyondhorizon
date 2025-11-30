package com.kenhorizon.libs.server;

import java.util.Objects;

@FunctionalInterface
public interface ModifiedNonNullUnaryOperator<T> extends ModifiedNonNullFunction<T, T> {

    static <T> ModifiedNonNullUnaryOperator<T> identity() {
        return t -> t;
    }

    default <V> ModifiedNonNullUnaryOperator<T> andThen(ModifiedNonNullUnaryOperator<T> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(apply(t));
    }
}
