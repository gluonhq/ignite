package com.gluonhq.ignite;


import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

public class GuiceApp extends Application implements ExampleApp {

    public static void main(String[] args) {
        launch(args);
    }

    private GuiceContext guiceContext = new GuiceContext(this, () -> Arrays.asList(new GuiceModule()));

    @Inject
    FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws IOException {
        guiceContext.init();
        fxmlLoader.setLocation(getViewLocation());
        Parent view = fxmlLoader.load();

        primaryStage.setTitle("Guice Example");
        primaryStage.setScene(new Scene(view));
        primaryStage.show();
    }


}

class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
//        bind(Service.class).to(Service.class);
    }
}
