package com.gluonhq.ignite.dagger;

import com.gluonhq.ignite.DIContext;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import javafx.fxml.FXMLLoader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of Dependency Injection context for Dagger
 */
public class DaggerContext implements DIContext {

    private final Object contextRoot;
    private final Supplier<Collection<Object>> modules;

    private ObjectGraph injector;

    /**
     * Create the Dagger context
     * @param contextRoot root object to inject
     * @param modules custom Dagger modules
     */
    public DaggerContext( Object contextRoot, Supplier<Collection<Object>> modules) {
        this.contextRoot = Objects.requireNonNull(contextRoot);
        this.modules = Objects.requireNonNull(modules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectMembers(Object instance) {
        injector.inject(instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getInstance(Class<T> cls) {
        return injector.get(cls);
    }

    /**
     * {@inheritDoc}
     */
    public final void init() {
        Collection<Object> allModules = new HashSet<>(modules.get());
        allModules.add(new FXModule());
        injector = ObjectGraph.create(allModules.toArray());
        injectMembers(contextRoot);
    }

    @Module(library = true, complete = false)
    class FXModule  {

        @Provides
        FXMLLoader provideFxmlLoader() {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(DaggerContext.this::getInstance);
            return loader;
        }

    }

}
