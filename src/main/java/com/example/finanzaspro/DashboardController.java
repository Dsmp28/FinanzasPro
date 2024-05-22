package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

        @FXML
        private Button btnInicio;

        @FXML
        private Button btnRegistrar;

        @FXML
        private Button btnTransaccion;

        @FXML
        private Button btnCuentas;

        @FXML
        private Button btnInversion;

        @FXML
        private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        botonActivo(btnInicio, "dashboard.fxml");
    }

    private void botonActivo(Button activo, String fxml){
        reiniciarEstilos();
        activo.setStyle("-fx-text-fill: #000000");
        cargarFxml(fxml);
    }
    private void reiniciarEstilos(){
        btnInicio.setStyle("-fx-text-fill: #55595f");
        btnRegistrar.setStyle("-fx-text-fill: #55595f");
        btnTransaccion.setStyle("-fx-text-fill: #55595f");
        btnCuentas.setStyle("-fx-text-fill: #55595f");
        btnInversion.setStyle("-fx-text-fill: #55595f");
    }
    private void cargarFxml(String fxml){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BtnInicio(){
        btnInicio.setOnAction(event -> {
            botonActivo(btnInicio, "dashboard.fxml");
        });
    }
    public void btnRegistro() {
        btnRegistrar.setOnAction(event -> {
            botonActivo(btnRegistrar, "registro-view.fxml");
        });
    }
}


