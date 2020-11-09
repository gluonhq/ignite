package com.gluonhq.ignite.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import javafx.fxml.FXMLLoader;

import javax.inject.Inject;

@Factory
public class FXMLLoaderFactory {

    @Inject private ApplicationContext ctx;

    @Prototype
    public FXMLLoader getLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(ctx::getBean);
        return loader;
    }

}
