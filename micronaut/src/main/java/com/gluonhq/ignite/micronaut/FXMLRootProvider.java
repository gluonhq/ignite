package com.gluonhq.ignite.micronaut;

import io.micronaut.context.ApplicationContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Singleton
public class FXMLRootProvider {

    @Inject private ApplicationContext ctx;

    public Node getRootByClass(@NotBlank Class<?> controllerClass) throws IOException {
        String viewName = getViewName(controllerClass);

        // Load node from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(ctx::getBean);

        Node node = //ctx.getBean(FXMLLoader.class)
            loader.load(controllerClass.getResourceAsStream(withExt(viewName, "fxml")));

        // Make sure that CSS is added to a scene that node is going to be added to
        node.sceneProperty().addListener((o, oldScene, newScene) -> Optional.ofNullable(newScene)
                .map(Scene::getStylesheets)
                .ifPresent( stylesheets ->
                Optional.ofNullable(controllerClass.getResource(withExt(viewName, "css")))
                        .map(URL::toExternalForm)
                        .filter(r -> !stylesheets.contains(r))
                        .ifPresent(stylesheets::add)
        ));
        return node;
    }

    private String getViewName(@NotBlank Class<?> controllerClass) {
        String className = removeSuffix(controllerClass.getName(), "Controller");
        return addSuffix(className, "View").replace('.', '/');
    }

    private String withExt( @NotBlank String viewName, @NotBlank String extName ) {
        String ext = extName.startsWith(".")? extName: "." + extName;
        return String.format("/%s%s", viewName, ext);
    }

    private static String removeSuffix( String s, String suffix ) {
        return s.endsWith(suffix)? s.substring(0, s.length() - suffix.length()): s;
    }

    private static String addSuffix( String s, String suffix ) {
        return s.endsWith(suffix)? s: s + suffix;
    }

}
