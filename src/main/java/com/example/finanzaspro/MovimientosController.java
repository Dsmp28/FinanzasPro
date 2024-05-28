package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MovimientosController implements Initializable, paneController{

    private DashboardController dashboardController;
    @FXML
    private Button btnRegistrar;

    @FXML
    private ComboBox cbMovimiento;

    @FXML
    private ListView lvCategorias;

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

        lvMovimientos.setOnMouseClicked(this::listViewDoubleClick);
    }

    public void btnRegistro(){
        btnRegistrar.setOnAction(event -> {
            if(dashboardController != null){
                dashboardController.cambiarVista("registro-view.fxml", dashboardController.getBtnRegistrar());
            }
        });
    }

    @Override
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    public void BuscarMovimientos(){
        BusquedaMovimientos BusquedaMovimientos = new BusquedaMovimientos(ManejadorMovimiento.getMovimientos(), (Categoria) lvCategorias.getSelectionModel().getSelectedItem(), cbMovimiento.getValue().toString(), txtTituloBusqueda.getText());
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
        categoriasFinal.add(new Categoria(0, "Todas las categorías", new Image(Objects.requireNonNull(ManejadorCategoria.class.getResource("icons/TodasCategorias.png")).toExternalForm())));

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

    private void listViewDoubleClick(MouseEvent event){
        if(event.getClickCount() == 2){
            Movimiento movimiento = (Movimiento) lvMovimientos.getSelectionModel().getSelectedItem();
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
            emergente.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/finanzas.png")).toString()));
            emergente.initStyle(javafx.stage.StageStyle.DECORATED);
            emergente.setResizable(false);
            emergente.setMaximized(false);
            emergente.setTitle("Editar movimiento");
            emergente.setScene(new Scene(root));
            editarController controller = loader.getController();
            controller.setStage(emergente);
            controller.setMovimiento(movimiento);
            emergente.showAndWait();

            CargarCategorias();
            CargarMovimientos();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
