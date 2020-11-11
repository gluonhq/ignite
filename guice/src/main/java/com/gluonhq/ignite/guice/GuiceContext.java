package com.gluonhq.ignite.guice;

import com.gluonhq.ignite.DIContext;
import com.google.inject.Module;
import com.google.inject.*;
import javafx.fxml.FXMLLoader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of dependency injection context for Guice
 */
public class GuiceContext implements DIContext {

    private final Object contextRoot;
    protected Injector injector;

    private final Supplier<Collection<Module>> modules;

    /**
     * Create the Guice context
     * @param contextRoot root object to inject
     * @param modules custom Guice modules
     */
    public GuiceContext( Object contextRoot, Supplier<Collection<Module>> modules ) {
        this.contextRoot = Objects.requireNonNull(contextRoot);
        this.modules = Objects.requireNonNull(modules);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void injectMembers(Object obj) {
        injector.injectMembers(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getInstance(Class<T> cls) {
        return injector.getInstance(cls);
    }

    /**
     * {@inheritDoc}
     */
    public final void init() {
        Collection<Module> uniqueModules = new HashSet<>(this.modules.get());
        uniqueModules.add(new FXModule());
        injector = Guice.createInjector(uniqueModules.toArray(new Module[0]));
        injectMembers(contextRoot);
    }

    private class FXModule extends AbstractModule {

        @Override
        protected void configure() {}

        @Provides
        FXMLLoader provideFxmlLoader() {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(GuiceContext.this::getInstance);
            return loader;
        }
    }

}
