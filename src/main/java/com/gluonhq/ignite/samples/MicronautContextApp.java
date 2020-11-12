package com.gluonhq.ignite.samples;

import com.gluonhq.ignite.micronaut.MicronautContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;

public class MicronautContextApp extends Application {

    @Inject
    View view;

    @Override
    public void init() throws Exception {
        // Create Micronaut context to initialize the app and make Application injectable
        new MicronautContext(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Micronaut Context Example");
        primaryStage.setScene(new Scene(view.getRoot()));
        primaryStage.show();
    }

}



