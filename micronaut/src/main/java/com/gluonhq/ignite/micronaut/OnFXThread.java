package com.gluonhq.ignite.micronaut;

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Around
@Type(com.gluonhq.ignite.micronaut.OnFxThreadInterceptor.class)
public @interface OnFXThread {
}
