package com.gluonhq.ignite;

import java.net.URL;

public interface ExampleApp {

    default URL getViewLocation() {
        return getClass().getResource("/com/gluonhq/ignite/view.fxml");
    }
}
