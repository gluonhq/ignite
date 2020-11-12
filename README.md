# Gluon Ignite   [![Build Status](https://travis-ci.com/gluonhq/ignite.svg?branch=master)](https://travis-ci.com/gluonhq/ignite)
Gluon Ignite allows developers to use popular dependency injection frameworks in their JavaFX applications, 
including inside their FXML controllers. Gluon Ignite creates a common abstraction over several popular 
dependency injection frameworks (currently _[Guice](https://github.com/google/guice), [Spring](https://spring.io/)_, 
, and _[Dagger](https://square.github.io/dagger/)_). 

Since v.1.3 we also added an extended support for and [Microunaut](https://micronaut.io). Read further for more details.  

With full support of [JSR-330](https://www.jcp.org/en/jsr/detail?id=330), Gluon Ignite makes using dependency injection in JavaFX applications trivial. 
Here is a quick example of creating an application using the Guice framework and Gluon Ignite.

```java
public class GuiceApp extends Application implements ExampleApp {
 
    public static void main(String[] args) {
        launch(args);
    }
 
    private GuiceContext context = new GuiceContext(this, () -> Arrays.asList(new GuiceModule()));
 
    @Inject private FXMLLoader fxmlLoader;
 
    @Override public void start(Stage primaryStage) throws IOException {
        context.init();
        fxmlLoader.setLocation(getViewLocation());
        Parent view = fxmlLoader.load();
 
        primaryStage.setTitle("Guice Example");
        primaryStage.setScene(new Scene(view));
        primaryStage.show();
    }
}
 
class GuiceModule extends AbstractModule {
    @Override protected void configure() {
        bind(Service.class).to(Service.class);
    }
}
```
By using Gluon Ignite, you not only get dependency injection inside your application class, but also 
within your FXML controllers too. Even though the sample above shows a Guice context, as mentioned 
Gluon Ignite also supports other DI frameworks. By using a DaggerContext or SpringContext in your application, 
it can be easily set up to work with those frameworks instead.

### Micronaut support

Ignite implementation for Micronaut provides much deeper integration with Micronaut framework.

#### _Application main class_

On top of common, for Ignite, implementation of `DIContext` interface, Ignite Micronaut provides 
special implementation of JavaFX `Application` class, which can to be configured 
as your main class: `com.gluonhq.ignite.micronaut.FXApplication`.
This makes the code a lot cleaner. As an application developer, you just have to implement 
an entry point into your application, which will be picked up by Micronaut automatically, 
since it is a normal Micronaut bean. 

Here is a simple example:
```java
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.runtime.event.annotation.EventListener;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.gluonhq.micronaut.FXApplication.StartEvent;
import static com.gluonhq.micronaut.FXApplication.StopEvent;

@Singleton
public class AppEntryPoint {

    @Inject
    private Object something;

    @EventListener
    void onAppStart(StartEvent event) {
        Stage stage = event.getStage();
        Scene scene = new Scene( new Label("Hello, world!"));
        stage.setScene(scene);
        stage.setTitle("My application");
        stage.show();
    }
    
    @EventListener
    void onAppStop(StopEvent event) {
        //...
    }

}
```
#### _JavaFX Application Thread_ 
Any bean method can be forced to run on JavaFX Application Thread using `OnFxThread` annotation
```java
@OnFXThread
void refreshTime() {
    label.setText(timePattern.format(ZonedDateTime.now()));
}
```
#### _FXML Views_ 
Ignite Micronaut implements support for FXML views based on naming conventions:
- A simple empty bean inherited from `FXMLView` defines the view, its name should be exactly 
  the same as corresponding `fxml` file and should be in the corresponding package. This name usually ends with `View` but does not have to.
- A bean named `<ViewName>Controller` defines a related FXML controller class. As per JavaFX standard, 
  it should be defined within `fxml` as well. The framework automatically makes it injectable.
- Optional stylesheet can be provided and should be located in the corresponding folder, 
  next to the `fxml` file, named as `<ViewName>.css`       
  
The view loaded by simply injecting the `View` class, and retrieving its root node using `getRoot` method.
Here is a simple example:
```java
package example.view;

@Singleton
public class ContentView extends BorderPane {

    @Inject
    private NavigationView navigationView;

    @Inject
    private StateView carStateView;
    // view      : example.view.StateView.java
    // controller: example.view.StateController.java
    // fxml      : example/view/StateView.fxml
    // stylesheet: example/view/StateView.css

    @PostConstruct
    private void init() {
        setCenter(navigationView);
        setLeft(carStateView.getRoot());
    }

}
```  

#### _Loading FXML directly_

FXML can be loaded using `FXMLLoader` bean. Controller class will also become injectable in this case.

```java

@Singleton
class SimpleLoad {

    @Inject
    FXMLLoader loader; // Note that the loader can only be used once.
    
    @PostConstruct
    private void init() {
       Node root = loader.load( SimpleLoad.class.getResourceAsStream("view.fxml"));
    }

}
``` 


### How to use Gluon Ignite

To use Gluon Ignite in your software, simply include it as a dependency in your preferred dependency manager. 
All artifacts are availble in Maven Central:

| Artifact Name | Version |
| ------------- | ------------- |
| Ignite Dagger | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-dagger.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-dagger)  |
| Ignite Guice  | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-guice.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-guice)  |
| Ignite Spring | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-spring.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-spring)  |
| Ignite Micronaut | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-micronaut.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-micronaut)  |

> Note that `ignite-common` is automatically included as a dependency for each module, 
so it is not necessary to include this as a dependency.
