package com.gluonhq.ignite.micronaut.view;

import com.gluonhq.ignite.micronaut.FXMLRootProvider;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Parent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Implementation of the View, which automatically loads the related XML file by convention.
 * Actual view should conform to following requirements
 * - inherit from FXMLView
 * - FXML file should have exactly the same name, but with ".fxml" extension
 * - The view and FXML file should be located in the same package
 *
 * @param <T> Root node type
 */
public class FXMLView<T extends Parent> implements View<T> {

    @Inject
    private FXMLRootProvider rootProvider;

    // rootProperty
    private final ReadOnlyObjectWrapper<T> rootProperty = new ReadOnlyObjectWrapper<>(this, "root");
    public ReadOnlyObjectProperty<T> rootProperty() {
       return rootProperty.getReadOnlyProperty();
    }

    @Override
    public final T getRoot() {
        return rootProperty().get();
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        rootProperty.set((T) rootProvider.getRootByClass(this.getClass()));
    }

}
