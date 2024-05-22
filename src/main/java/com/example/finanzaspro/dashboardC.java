package com.example.finanzaspro;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class dashboardC implements Initializable {
    @FXML
    private Label txtPresupuesto;

    @FXML
    private ListView<Movimiento> lvListaMovimientos;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CargarPresupuesto();
        CargarMovimientos();
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
    }
}
