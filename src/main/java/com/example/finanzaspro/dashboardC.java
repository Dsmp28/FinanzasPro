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

    @FXML
    private Label txtIngresado;

    @FXML
    private Label txtGasto;

    @FXML
    private Button btnTodos;

    @FXML
    private Button btnGastos;

    @FXML
    private Button btnIngresos;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MostrarTodosMovimientos();
        CargarIngresado();
        CargarEgresos();
        CargarPresupuesto();
    }

    @FXML
    public void MostrarTodosMovimientos(){
        botonActivo(btnTodos);
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        lvListaMovimientos.itemsProperty().bind(Bindings.createObjectBinding(() -> movimientos, movimientos));
        lvListaMovimientos.setCellFactory(listView -> new MovimientoCell());
    }

    @FXML
    public void MostrarGastos(){
        botonActivo(btnGastos);
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        lvListaMovimientos.itemsProperty().bind(Bindings.createObjectBinding(() -> movimientos, movimientos));
        lvListaMovimientos.setCellFactory(listView -> new MovimientoCell());
    }

    @FXML
    public void MostrarIngresos(){
        botonActivo(btnIngresos);
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        lvListaMovimientos.itemsProperty().bind(Bindings.createObjectBinding(() -> movimientos, movimientos));
        lvListaMovimientos.setCellFactory(listView -> new MovimientoCell());
    }

    private void CargarPresupuesto(){
        Presupuesto presupuesto = ControladorPresupuesto.getInstancia().getPresupuesto();
        StringBinding textoPrespupuesto = Bindings.createStringBinding(() -> String.format("Q. %.2f", presupuesto.montoProperty().get()), presupuesto.montoProperty());
        txtPresupuesto.textProperty().bind(textoPrespupuesto);
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

    private void botonActivo(Button activo){
        reiniciarEstilos();
        activo.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-background-color: #ccffff;");
    }

    private void reiniciarEstilos(){
        btnTodos.setStyle("-fx-text-fill: #55595f");
        btnGastos.setStyle("-fx-text-fill: #55595f");
        btnIngresos.setStyle("-fx-text-fill: #55595f");
    }
}
