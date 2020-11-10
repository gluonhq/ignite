package com.gluonhq.ignite.micronaut.view;

import javafx.scene.Parent;

public interface View<T extends Parent> {
    T getRoot();
}