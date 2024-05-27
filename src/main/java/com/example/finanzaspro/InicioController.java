package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InicioController {
    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtPresupuesto;

    @FXML
    public void btnIngresarClick() {
        String correo = txtCorreo.getText();
        String presupuesto = txtPresupuesto.getText();

        if (correo.isEmpty() || presupuesto.isEmpty()) {
            ManejadorAlertas.showError("Error", "Campos vacíos", "Por favor, llene ambos campos");
        }else if (!esCorreoValido(correo)){
            ManejadorAlertas.showError("Error", "Correo inválido", "Por favor, ingrese un correo válido");
        }else {
            try {
                double monto = Double.parseDouble(presupuesto);
                if (monto <= 0) {
                    ManejadorAlertas.showError("Error", "Presupuesto inválido", "El presupuesto debe ser mayor a 0");
                }else {
                    if (monto > 999999999999999.0){
                        ManejadorAlertas.showError("Error", "Presupuesto inválido", "El presupuesto debe ser menor a 999999999999999");
                    }else {
                        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "DatoPresupuesto.json");
                        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "PresupuestoOriginal.json");
                        ManejadorEncriptacion.guardarCorreoEnJSON(correo, "DatoCorreo.json");
                        ManejadorAlertas.showInformation("Éxito", "Presupuesto guardado", "El presupuesto se ha guardado correctamente");
                        abrirDashboard();
                    }
                }
            } catch (NumberFormatException e) {
                ManejadorAlertas.showError("Error", "Presupuesto inválido", "El presupuesto debe ser un número válido");
            }
        }
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public void abrirDashboard(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("FinanzasProDashBoard");
            stage.show();
            txtCorreo.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
