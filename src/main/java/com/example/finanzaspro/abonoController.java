package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class abonoController {

    private int currentIndex;

    @FXML
    private TextField txtCantidad;

    private Stage stage;

    public void setInversion(int currentIndex){
        this.currentIndex = currentIndex;
        double mensualidad = ManejadorInversion.getInversiones().get(currentIndex).calcularMensualidad();
        txtCantidad.setText(String.format("%.2f", mensualidad));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void cerrar(){
        stage.close();
    }

    @FXML
    private void agregarAbono(){
        if(Validar()){
            double cantidad = Double.parseDouble(txtCantidad.getText());
            ManejadorInversion.agregarAbono(currentIndex, cantidad);
            ManejadorAlertas.showInformation("Exito", "Abono agregado", "Se ha agregado el abono exitosamente");
            cerrar();
        }
    }

    private boolean Validar(){
        if(txtCantidad.getText().isEmpty()){
            ManejadorAlertas.showError("Error", "Caja de texto vacia", "Por favor ingrese una cantidad para abonar");
            return false;
        }else{
            try {
                double cantidad = Double.parseDouble(txtCantidad.getText());
                if(cantidad <= 0 || cantidad > 999999999999999.0){
                    ManejadorAlertas.showError("Error", "Cantidad invalida", "Por favor ingrese una cantidad mayor a 0 y menor a 999,999,999,999,999");
                    return false;
                }
            } catch (NumberFormatException e) {
                ManejadorAlertas.showError("Error", "Cantidad invalida", "Por favor ingrese una cantidad valida");
                return false;
            }
        }
        return true;
    }
}
