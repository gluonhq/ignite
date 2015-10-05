package com.gluonhq.ignite;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @Inject Service service;
    @FXML Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText(service.getText());
    }


}
