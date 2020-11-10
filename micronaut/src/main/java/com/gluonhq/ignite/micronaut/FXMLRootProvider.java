package com.gluonhq.ignite.micronaut;

import io.micronaut.context.ApplicationContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Singleton
public class FXMLRootProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FXMLRootProvider.class);

    @Inject private ApplicationContext ctx;
    @Inject FXMLLoader loader;

    public Node getRootByClass(@NotBlank Class<?> viewClass) {
        String viewPath = getViewPath(viewClass);

        String fxml = withExt(viewPath, "fxml");
        Node node = null;

        try {
            LOG.info("Attempting to load " + fxml);
            node = loader.load(viewClass.getResourceAsStream(fxml));
        } catch (IOException e) {
            throw new RuntimeException("Could find resource " + fxml, e);
        }

        // Make sure that CSS is added to a scene that node is going to be added to
        node.sceneProperty().addListener((o, oldScene, newScene) -> Optional.ofNullable(newScene)
                .map(Scene::getStylesheets)
                .ifPresent( stylesheets ->
                        Optional.ofNullable(viewClass.getResource(withExt(viewPath, "css")))
                                .map(URL::toExternalForm)
                                .filter(r -> !stylesheets.contains(r))
                                .ifPresent(stylesheets::add)
                ));
        return node;
    }

    private String getViewPath(@NotBlank Class<?> cls) {
        return cls.getName().replace('.', '/');
    }

    private String withExt( @NotBlank String viewName, @NotBlank String extName ) {
        String ext = extName.startsWith(".")? extName: "." + extName;
        return String.format("/%s%s", viewName, ext);
    }

}
