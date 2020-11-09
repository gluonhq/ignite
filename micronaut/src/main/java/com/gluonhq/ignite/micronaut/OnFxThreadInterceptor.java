package com.gluonhq.ignite.micronaut;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import javafx.application.Platform;

import javax.inject.Singleton;

@Singleton
class OnFxThreadInterceptor implements MethodInterceptor<Object, Object> {
    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        if ( !Platform.isFxApplicationThread() ) {
            Platform.runLater(context::proceed);
            return null;
        }
        return context.proceed();
    }
}
