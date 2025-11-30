package com.kenhorizon.libs.server;

import net.minecraftforge.common.util.Lazy;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface ModifiedNonNullSupplier<@NonnullType T> extends Supplier<T> {

    @Override
    T get();

    static <T> ModifiedNonNullSupplier<T> of(Supplier<@NullableType T> sup) {
        return of(sup, () -> "Unexpected null value from supplier");
    }

    static <T> ModifiedNonNullSupplier<T> of(Supplier<@NullableType T> sup, ModifiedNonNullSupplier<String> errorMsg) {
        return () -> {
            T res = sup.get();
            Objects.requireNonNull(res, errorMsg);
            return res;
        };
    }

    default ModifiedNonNullSupplier<T> lazy() {
        return lazy(this);
    }

    static <T> ModifiedNonNullSupplier<T> lazy(Supplier<@NonnullType T> sup) {
        return Lazy.of(sup)::get;
    }
}
