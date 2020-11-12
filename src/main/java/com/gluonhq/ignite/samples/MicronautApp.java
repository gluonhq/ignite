package com.gluonhq.ignite.samples;

import com.gluonhq.ignite.micronaut.FXApplication;
import com.gluonhq.ignite.micronaut.view.FXMLView;
import io.micronaut.runtime.event.annotation.EventListener;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MicronautApp {

    @Inject
    View view;

    @EventListener
    void onAppStart(FXApplication.StartEvent event) {
        Stage primaryStage = event.getStage();
        primaryStage.setTitle("Micronaut Example");
        primaryStage.setScene(new Scene(view.getRoot()));
        primaryStage.show();
    }
}
