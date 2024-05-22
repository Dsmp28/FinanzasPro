package com.example.finanzaspro;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class dashboardC implements Initializable {
    @FXML
    private Label txtPresupuesto;

    @FXML
    private ListView<Movimiento> lvListaMovimientos;

    @FXML
    private Label txtIngresado;

    @FXML
    private Label txtGasto;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CargarMovimientos();
        CargarIngresado();
        CargarEgresos();
        CargarPresupuesto();

        lvListaMovimientos.setOnMouseClicked(this::listViewDoubleClick);
    }

    private void CargarPresupuesto(){
        Presupuesto presupuesto = ControladorPresupuesto.getInstancia().getPresupuesto();
        StringBinding textoPrespupuesto = Bindings.createStringBinding(() -> String.format("Q. %.2f", presupuesto.montoProperty().get()), presupuesto.montoProperty());
        txtPresupuesto.textProperty().bind(textoPrespupuesto);
    }

    private void CargarMovimientos(){
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        lvListaMovimientos.itemsProperty().bind(Bindings.createObjectBinding(() -> movimientos, movimientos));
        lvListaMovimientos.setCellFactory(listView -> new MovimientoCell());
        lvListaMovimientos.setFocusTraversable(false);
    }

    private void CargarIngresado(){
        Ingreso ingreso = ManejadorIngreso.getInstancia().getIngreso();
        StringBinding textoIngresado = Bindings.createStringBinding(() -> String.format("Q. %.2f", ingreso.montoProperty().get()), ingreso.montoProperty());
        txtIngresado.textProperty().bind(textoIngresado);
    }

    private void CargarEgresos(){
        Egreso egreso = ManejadorEgresos.getInstancia().getEgreso();
        StringBinding textoEgresado = Bindings.createStringBinding(() -> String.format("Q. %.2f", egreso.montoProperty().get()), egreso.montoProperty());
        txtGasto.textProperty().bind(textoEgresado);
    }

    private void listViewDoubleClick(MouseEvent event){
        if(event.getClickCount() == 2){
            Movimiento movimiento = lvListaMovimientos.getSelectionModel().getSelectedItem();
            if(movimiento != null){
                abrirEdicion(movimiento);
            }
        }
    }
    private void abrirEdicion(Movimiento movimiento){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editar-view.fxml"));
            Parent root = loader.load();
            Stage emergente = new Stage();

            emergente.initModality(Modality.APPLICATION_MODAL);
            emergente.setTitle("Editar movimiento");
            emergente.setScene(new Scene(root));
            editarController controller = loader.getController();
            controller.setStage(emergente);
            controller.setMovimiento(movimiento);
            emergente.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
