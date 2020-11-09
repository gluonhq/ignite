package com.gluonhq.ignite.micronaut.view;

import com.gluonhq.ignite.micronaut.FXMLRootProvider;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Parent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Implementation of the View, which automatically loads the related
 * FXML file by convention. FXML file should have same name as a view and be in the same package.
 * @param <T> ttype if the root node.
 */
public class FXMLView<T extends Parent> implements View<T> {

    @Inject
    private FXMLRootProvider rootProvider;

    // rootProperty
    private final ReadOnlyObjectWrapper<T> rootProperty = new ReadOnlyObjectWrapper<>(this, "root");
    public ReadOnlyObjectProperty<T> rootProperty() {
       return rootProperty.getReadOnlyProperty();
    }

    public final T getRoot() {
       return rootProperty().get();
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        try {
            rootProperty.set((T) rootProvider.getRootByClass(this.getClass()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
