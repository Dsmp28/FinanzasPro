package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    @FXML
    private ChoiceBox<String> cbMovimiento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbMovimiento.getItems().add("Ingreso");
        cbMovimiento.getItems().add("Egreso");
        cbMovimiento.setValue("Ingreso");
        cbMovimiento.setStyle("-fx-text-fill: white");
    }
}
