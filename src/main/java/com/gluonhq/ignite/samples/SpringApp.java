package com.gluonhq.ignite.samples;

import com.gluonhq.ignite.spring.SpringContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;

public class SpringApp extends Application implements ExampleApp {

    public static void main(String[] args) {
        launch(args);
    }

    private final SpringContext context = new SpringContext(this,
            () -> Collections.singletonList(SpringApp.class.getPackage().getName()));


    @Inject
    FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws IOException {
        context.init();
        fxmlLoader.setLocation(getViewLocation());
        Parent view = fxmlLoader.load();

        primaryStage.setTitle("Spring Example");
        primaryStage.setScene(new Scene(view));
        primaryStage.show();
    }
}

@Configuration
class SpringConfig  {
    @Bean
    public Service provideService() {
        return new Service();
    }
}

