package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;

public class MovimientosController implements Initializable, paneController{

    private DashboardController dashboardController;
    @FXML
    private Button btnRegistrar;

    @FXML
    private ComboBox cbMovimiento;

    @FXML
    private ListView lvCategorias;

    @FXML
    private Button btnBusqueda;

    @FXML
    private ListView lvMovimientos;

    @FXML
    private TextField txtTituloBusqueda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnRegistro();
        cargarComboBox();
        CargarCategorias();
        CargarMovimientos();
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

    @FXML
    public void BuscarMovimientos(){
        BusquedaMovimientos BusquedaMovimientos = new BusquedaMovimientos(ManejadorMovimiento.getMovimientos(), (Categoria) lvCategorias.getSelectionModel().getSelectedItem(), cbMovimiento.getValue().toString(), txtTituloBusqueda.getText().toString());
        ObservableList<Movimiento> movimientosEncontrados = BusquedaMovimientos.BuscarMovimientos();
        lvMovimientos.itemsProperty().bind(Bindings.createObjectBinding(() -> movimientosEncontrados, movimientosEncontrados));
        lvMovimientos.setCellFactory(listView -> new MovimientoCell());
    }

    private void cargarComboBox(){
        cbMovimiento.getItems().addAll("Ingreso", "Egreso", "Todos");
        cbMovimiento.setValue("Todos");
        cbMovimiento.setStyle("-fx-text-fill: white");
    }

    private void CargarCategorias() {
        ObservableList<Categoria> categorias = ManejadorCategoria.getCategorias();
        ObservableList<Categoria> categoriasFiltradas = categorias.filtered(categoria -> !categoria.getTitulo().equals("Otra"));

        // Crear una nueva lista y agregar la opción "Todas las categorías" primero
        ObservableList<Categoria> categoriasFinal = FXCollections.observableArrayList();
        categoriasFinal.add(new Categoria(0, "Todas las categorías", new Image(ManejadorCategoria.class.getResource("icons/TodasCategorias.png").toExternalForm())));

        // Agregar las demás categorías a la nueva lista
        categoriasFinal.addAll(categoriasFiltradas);

        lvCategorias.itemsProperty().bind(Bindings.createObjectBinding(() -> categoriasFinal, categoriasFinal));
        lvCategorias.setCellFactory(listView -> new CategoriaCell());
    }

    private void CargarMovimientos(){
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        lvMovimientos.itemsProperty().bind(Bindings.createObjectBinding(() -> movimientos, movimientos));
        lvMovimientos.setCellFactory(listView -> new MovimientoCell());
    }
}
