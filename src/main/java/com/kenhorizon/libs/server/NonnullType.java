package com.kenhorizon.libs.server;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * An alternative to {@link Nonnull} which works on type parameters (J8 feature).
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
public @interface NonnullType {
}
