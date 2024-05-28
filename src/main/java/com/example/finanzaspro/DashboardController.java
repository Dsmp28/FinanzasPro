package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardController implements Initializable {

        @FXML
        private Button btnInicio;

        @FXML
        private Button btnRegistrar;

        @FXML
        private Button btnTransaccion;

        @FXML
        private Button btnSalir;

        @FXML
        private Button btnInversion;

        @FXML
        private Button btnEstadistica;

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
        btnEstadistica.setStyle("-fx-text-fill: #55595f");
        btnInversion.setStyle("-fx-text-fill: #55595f");
    }
    private void cargarFxml(String fxml){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            if(loader.getController() instanceof paneController){
                paneController controller = loader.getController();
                controller.setDashboardController(this);
            }
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BtnInicio(){
        btnInicio.setOnAction(event -> botonActivo(btnInicio, "dashboard.fxml"));
    }
    public void btnRegistro() {
        btnRegistrar.setOnAction(event -> botonActivo(btnRegistrar, "registro-view.fxml"));
    }
    public void btnTransaccion() {
        btnTransaccion.setOnAction(event -> botonActivo(btnTransaccion, "movimientos-view.fxml"));
    }
    public void btnInversion(){
        btnInversion.setOnAction(event -> botonActivo(btnInversion, "inversion-view.fxml"));
    }
    public void btnEstadistica(){
        btnEstadistica.setOnAction(event -> botonActivo(btnEstadistica, "estadisticas-view.fxml"));
    }
    public void btnConfiguracion(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Configuración");
        dialog.setHeaderText("Cambiar email");
        dialog.setContentText("Escriba el email nuevo o actualice el anterior:");
        dialog.getEditor().setText(ManejadorEncriptacion.leerCorreoDeJSON("DatoCorreo.json"));
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("cssestadisticas.css")).toExternalForm());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            if (!email.isEmpty()){
                if (esCorreoValido(email)) {
                    ManejadorEncriptacion.guardarCorreoEnJSON(email, "DatoCorreo.json");
                    ManejadorAlertas.showInformation("Éxito", "Email guardado", "El email se ha guardado correctamente");
                }else {
                    ManejadorAlertas.showError("Error", "Email inválido", "Por favor, ingrese un email válido");
                }
            }else {
                ManejadorAlertas.showError("Error", "Campo vacio", "Por favor, llene el campo de texto");
            }
        });
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }


    public void btnSalir(){
        btnSalir.setOnAction(event -> salir());
    }
    private void salir(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Estás seguro de que deseas salir?");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("cssestadisticas.css")).toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            Stage stage = (Stage) btnSalir.getScene().getWindow();
            stage.close();
        }
    }

    public void cambiarVista(String fxml, Button activo){
        botonActivo(activo, fxml);
    }

    public Button getBtnRegistrar(){
        return btnRegistrar;
    }
    public Button getBtnTransaccion(){
        return btnTransaccion;
    }
    public Button getBtnInversion(){
        return btnInversion;
    }
}


