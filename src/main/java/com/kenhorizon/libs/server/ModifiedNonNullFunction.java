package com.kenhorizon.libs.server;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface ModifiedNonNullFunction<@NonnullType T, @NonnullType R> extends Function<T, R> {

	@Override
	R apply(T t);

	default <V> ModifiedNonNullFunction<T, V> andThen(ModifiedNonNullFunction<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return t -> after.apply(apply(t));
	}
}
