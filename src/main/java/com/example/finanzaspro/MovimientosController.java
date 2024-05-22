package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MovimientosController implements Initializable, paneController{

    private DashboardController dashboardController;
    @FXML
    private Button btnRegistrar;

    @FXML
    private ComboBox cbMovimiento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnRegistro();
        cargarComboBox();
    }
    public void btnRegistro(){
        btnRegistrar.setOnAction(event -> {
            if(dashboardController != null){
                dashboardController.cambiarVista("registro-view.fxml", dashboardController.getBtnRegistrar());
            };
        });
    }

    @Override
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void cargarComboBox(){
        cbMovimiento.getItems().addAll("Ingreso", "Egreso", "Todos");
        cbMovimiento.setValue("Todos");
        cbMovimiento.setStyle("-fx-text-fill: white");
    }
}
