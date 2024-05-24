package com.example.finanzaspro;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InversionController implements Initializable {

    private int currentIndex;
    private ObservableList<Inversion> inversiones;

    @FXML
    private Label lbAbonado;

    @FXML
    private Label lbGanado;

    @FXML
    private Label lbValorActual;

    @FXML
    private Label lbPorcentaje;

    @FXML
    private Label lbMeta;

    @FXML
    private Label lbMesesAbonados;

    @FXML
    private Label lbConsejo1;

    @FXML
    private Label lbRetornoActual;

    @FXML
    private ProgressIndicator piProgreso;

    @FXML
    private Circle circuloFondo;

    @FXML
    private VBox vbPanelPorcentaje;

    @FXML
    private Button btnAbonar;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnSiguiente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CargarInversion();
    }

    @FXML
    private void abrirVentanaAgregarInversion() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevaInv-view.fxml"));
            Parent root = loader.load();
            Stage emergente = new Stage();

            emergente.initModality(Modality.APPLICATION_MODAL);
            emergente.initStyle(javafx.stage.StageStyle.UTILITY);
            emergente.setTitle("Agregar nueva Inversión");
            emergente.setScene(new Scene(root));
            nuevaInvController controller = loader.getController();
            controller.setStage(emergente);
            emergente.showAndWait();

            CargarInversion();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void pruebaAbono(){
        Inversion inversion = inversiones.get(currentIndex);
        ManejadorInversion.agregarAbono(currentIndex, 3141.27);
        mostrarInversion();
    }

    @FXML
    private void siguienteInversion(){
        if (currentIndex < inversiones.size() - 1) {
            currentIndex++;
            mostrarInversion();
        }
    }

    @FXML
    private void anteriorInversion(){
        if (currentIndex > 0) {
            currentIndex--;
            mostrarInversion();
        }
    }

    private void CargarInversion(){
        inversiones = ManejadorInversion.getInversiones();
        currentIndex = inversiones.size() - 1;
        if (inversiones.isEmpty()){
            lbAbonado.setText("Q. 0.0");
            lbGanado.setText("Q. 0.0");
            lbValorActual.setText("Q. 0.0");
            lbPorcentaje.setText("0%");
            lbMeta.setText("Q0.0");
            piProgreso.setProgress(0);
            lbMesesAbonados.setText("0 / 0 meses");
            lbConsejo1.setText("No tienes inversiones registradas");
            lbRetornoActual.setText("Retorno actual (0%)");
        } else {
            mostrarInversion();
        }
    }

    private void mostrarInversion(){
        Inversion inversion = inversiones.get(currentIndex);
        lbAbonado.setText("Q. " + String.format("%.2f", inversion.calcularTotalDineroAnadido()));
        lbGanado.setText("Q. " + String.format("%.2f", inversion.calcularDineroGanado()));
        lbValorActual.setText("Q. " + String.format("%.2f", inversion.getValorActual()));
        lbMesesAbonados.setText(inversion.getAbonosMensuales().size() + " / " + inversion.getPlazoMeses() + " meses");
        lbRetornoActual.setText("Retorno actual (" + (int) Math.round(inversion.getTasaRetorno() * 100) + "%)");
        if (ValidarInversion(inversion)){
            btnAbonar.setDisable(true);
        }else {
            btnAbonar.setDisable(false);
        }
    }

    private boolean ValidarInversion(Inversion inversion){
        int porcentaje = (int) Math.round(inversion.getValorActual() / inversion.getMontoMeta() * 100);
        if (porcentaje >= 100){
            lbConsejo1.setText("¡Felicidades! Has cumplido con tu meta de " + String.format("%.2f", inversion.getMontoMeta()) + " Q");
            piProgreso.setProgress(1);
            circuloFondo.visibleProperty().setValue(false);
            vbPanelPorcentaje.visibleProperty().setValue(false);
            return true;
        }

        lbPorcentaje.setText(porcentaje + "%");
        lbMeta.setText("Q" + String.format("%.2f", inversion.getMontoMeta()));
        piProgreso.setProgress(inversion.getValorActual() / inversion.getMontoMeta());

        if (inversion.getAbonosMensuales().size() > inversion.getPlazoMeses()){
            lbConsejo1.setText("¡Cuidado! Has abonado más meses de los necesarios, revisa si tus abonos no estan generando perdidas");
        } else if (porcentaje > 50){
            lbConsejo1.setText("¡Vas por buen camino! Sigue abonando " +  String.format("%.2f", inversion.calcularMensualidad()) + " al mes para cumplir con tu meta");
        } else {
            lbConsejo1.setText("Para poder cumplir con tu meta en el tiempo esperado debes de abonar  Q. " + String.format("%.2f", inversion.calcularMensualidad()) + " al mes ");
        }

        circuloFondo.visibleProperty().setValue(true);
        vbPanelPorcentaje.visibleProperty().setValue(true);

        return false;
    }
}
