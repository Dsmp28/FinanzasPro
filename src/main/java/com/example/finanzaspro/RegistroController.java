package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    @FXML
    private ChoiceBox<String> cbMovimiento;

    @FXML
    private CheckBox cbRecurrente;

    @FXML
    private Label labelDias;

    @FXML
    private TextField txtDias;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtDias.visibleProperty().bind(cbRecurrente.selectedProperty());
        labelDias.visibleProperty().bind(cbRecurrente.selectedProperty());
        cbMovimiento.getItems().add("Ingreso");
        cbMovimiento.getItems().add("Egreso");
        cbMovimiento.setValue("Ingreso");
        cbMovimiento.setStyle("-fx-text-fill: white");
    }
}
