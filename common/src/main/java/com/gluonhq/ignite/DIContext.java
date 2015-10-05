package com.gluonhq.ignite;


/**
 * Common definition of Dependecy Injection Context
 */
public interface DIContext {

    /**
     * Injects members into given instance
     * @param instance instance to inject members into
     */
    void injectMembers(Object instance);

    /**
     * Create instance of given class
     * @param cls
     * @param <T>
     * @return resulting instance
     */
    <T> T getInstance(Class<T> cls);

    /**
     * Context initialization
     */
    default void init() {
        // no-op
    }

    /**
     * Context disposal
     */
    default void dispose() {
        // no-op
    }
}