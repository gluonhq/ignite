package com.gluonhq.ignite.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.LifeCycle;
import io.micronaut.context.event.ApplicationEventPublisher;
import javafx.stage.Stage;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public final class FXApplication extends javafx.application.Application {

    private final ApplicationContext appContext = ApplicationContext.build().start();
    private final ApplicationEventPublisher publisher = appContext.getBean(ApplicationEventPublisher.class);

    @Override
    public void init()  {
        publisher.publishEvent(new InitEvent());
    }

    @Override
    public void start(Stage primaryStage) {
        publisher.publishEvent(new StartEvent(primaryStage));
    }

    @Override
    public void stop() {
        publisher.publishEvent(new StopEvent());
        Optional.of(appContext)
                .filter(LifeCycle::isRunning)
                .ifPresent(ApplicationContext::stop);
    }

    public static class InitEvent{}

    public static class StartEvent {
        private final Stage stage;

        public StartEvent(Stage stage) {
            this.stage = stage;
        }

        public Stage getStage() {
            return stage;
        }
    }

    public static class StopEvent{}

}



