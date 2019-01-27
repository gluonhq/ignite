# Gluon Ignite   [![Build Status](https://travis-ci.com/gluonhq/ignite.svg?branch=master)](https://travis-ci.com/gluonhq/ignite)
Gluon Ignite allows developers to use popular dependency injection frameworks in their JavaFX applications, 
including inside their FXML controllers. Gluon Ignite creates a common abstraction over several popular 
dependency injection frameworks (currently _[Guice](https://github.com/google/guice), [Spring](https://spring.io/)_, 
and _[Dagger](https://square.github.io/dagger/)_, but we plan at add more as the demand becomes obvious). 

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
it can be easily set up to work with with those frameworks instead.

To use Gluon Ignite in your software, simply include it as a dependency in your preferred dependency manager. 
All artifacts are availble in Maven Central:

| Artifact Name | Version |
| ------------- | ------------- |
| Ignite Dagger | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-dagger.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-dagger)  |
| Ignite Guice  | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-guice.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-guice)  |
| Ignite Spring | [![xxx](https://img.shields.io/maven-central/v/com.gluonhq/ignite-spring.svg)](https://search.maven.org/artifact/com.gluonhq/ignite-spring)  |


> Note that `ignite-common` is automatically included as a dependency for each module, 
so it is not necessary to include this as a dependency.
