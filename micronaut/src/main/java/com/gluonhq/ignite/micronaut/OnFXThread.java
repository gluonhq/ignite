package com.gluonhq.ignite.micronaut;

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;

import java.lang.annotation.*;

/**
 * Forces a method to run on JavaFX Application Thread
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Around
@Type(OnFXThreadInterceptor.class)
public @interface OnFXThread {
}
