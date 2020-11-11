package com.gluonhq.ignite.samples;

import java.net.URL;

public interface ExampleApp {

    default URL getViewLocation() {
        return getClass().getResource("/com/gluonhq/ignite/samples/View.fxml");
    }
}
