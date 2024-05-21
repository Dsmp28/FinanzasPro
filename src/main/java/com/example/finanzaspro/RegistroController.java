package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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

    @FXML
    private Label txtPresupuesto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtDias.visibleProperty().bind(cbRecurrente.selectedProperty());
        labelDias.visibleProperty().bind(cbRecurrente.selectedProperty());
        cbMovimiento.getItems().add("Ingreso");
        cbMovimiento.getItems().add("Egreso");
        cbMovimiento.setValue("Ingreso");
        cbMovimiento.setStyle("-fx-text-fill: white");
        CargarPresupuesto();
    }

    private void CargarPresupuesto(){
        Presupuesto presupuesto = ControladorPresupuesto.getInstancia().getPresupuesto();

        StringBinding textoPrespupuesto = Bindings.createStringBinding(() -> String.format("Q. %.2f", presupuesto.montoProperty().get()), presupuesto.montoProperty());

        txtPresupuesto.textProperty().bind(textoPrespupuesto);
    }
}
