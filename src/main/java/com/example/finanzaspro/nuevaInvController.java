package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class nuevaInvController {
    @FXML
    private TextField txtMonto;

    @FXML
    private TextField txtPlazo;

    @FXML
    private TextField txtTasa;

    @FXML
    private TextField txtTitulo;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void cerrar(){
        stage.close();
    }

    @FXML
    private void agregarInversion() {
        if (validarCampos()) {
            double monto = Double.parseDouble(txtMonto.getText());
            int plazo = Integer.parseInt(txtPlazo.getText());
            double tasa = Double.parseDouble(txtTasa.getText());
            String titulo = txtTitulo.getText();
            ManejadorInversion.agregarInversion(monto, tasa, plazo, titulo);
            ManejadorAlertas.showInformation("Inversión agregada", "Inversión agregada", "La inversión ha sido agregada exitosamente");
            stage.close();
        }
    }

    private boolean validarCampos() {
        if (txtMonto.getText().isEmpty() || txtPlazo.getText().isEmpty() || txtTasa.getText().isEmpty() || txtTitulo.getText().isEmpty()) {
            ManejadorAlertas.showError("Error", "Faltan datos obligatorios", "Por favor, llene todos los campos obligatorios");
            return false;
        } else {
            try {
                if (Double.parseDouble(txtTasa.getText()) <= 0 || Double.parseDouble(txtTasa.getText()) > 999999999999999.0){
                    ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número mayor a 0 y menor a 999,999,999,999,999 en el campo de tasa de retorno");
                    return false;
                } else if (Integer.parseInt(txtPlazo.getText()) <= 0) {
                    ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número mayor a 0 en el campo de plazo para llenarlo");
                    return false;
                }else if (Double.parseDouble(txtMonto.getText()) <= 0) {
                    ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número mayor a 0 en el campo de monto meta");
                    return false;
                }
            } catch (NumberFormatException e) {
                ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número válido en el campo de tasa de retorno");
                return false;
            }
        }
        return true;
    }
}
