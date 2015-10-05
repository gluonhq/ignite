package com.gluonhq.ignite;


import com.gluonhq.ignite.dagger.DaggerContext;
import dagger.Module;
import dagger.Provides;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

public class DaggerApp extends Application implements ExampleApp {

    private final DaggerContext context = new DaggerContext(this, () -> Arrays.asList(new DaggerModule()));

    @Inject
    FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        context.init();

        fxmlLoader.setLocation( getViewLocation());
        Parent view = fxmlLoader.load();

        primaryStage.setTitle("Dagger Example");
        primaryStage.setScene(new Scene(view));
        primaryStage.show();

    }

}

@Module( library = true, injects = {DaggerApp.class,ViewController.class}, complete = false)
class DaggerModule  {

    @Provides
    public Service provideService() {
        return new Service();
    }

}

