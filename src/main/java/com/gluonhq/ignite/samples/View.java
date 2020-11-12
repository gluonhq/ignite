package com.gluonhq.ignite.samples;

import com.gluonhq.ignite.micronaut.view.FXMLView;
import javafx.scene.layout.BorderPane;

import javax.inject.Singleton;

// View.xml is automatically loaded
@Singleton
class View extends FXMLView<BorderPane> {
}
