package com.gluonhq.ignite.micronaut;

import com.gluonhq.ignite.DIContext;
import io.micronaut.context.ApplicationContext;

import java.util.Objects;

/**
 * Implementation of Dependency Injection context for Micronaut
 */
public final class MicronautContext implements DIContext {

    private final ApplicationContext appContext = ApplicationContext.build().start();

    /**
     * Create the Micronaut context
     * @param contextRoot root object to inject
     */
    public MicronautContext( Object contextRoot ) {
        injectMembers( Objects.requireNonNull(contextRoot));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectMembers(Object instance) {
        appContext.inject(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getInstance(Class<T> cls) {
        return appContext.getBean(cls);
    }
}