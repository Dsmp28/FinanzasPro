package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    @FXML
    private ChoiceBox<String> cbMovimiento;

    @FXML
    private CheckBox cbRecurrente;

    @FXML
    private Label labelDias;

    @FXML
    private TextField txtDias;

    @FXML
    private Label txtPresupuesto;

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private Label lblSubCategoria;

    @FXML
    private TextField txtSubcategoria;

    @FXML
    private Button btnIngreso;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CargarRecurrente();
        CargarComboBoxTipoMovimiento();
        CargarPresupuesto();
        CargarComboBoxCategoria();
    }

    @FXML
    public void AgregarSeccionNuevaCategoria(){
        Categoria categoriaSeleccionada = cbCategoria.getSelectionModel().getSelectedItem();

        if (categoriaSeleccionada != null && "Otra".equals(categoriaSeleccionada.getTitulo())){
            lblSubCategoria.setVisible(true);
            txtSubcategoria.setText("");
            txtSubcategoria.setVisible(true);
        } else {
            lblSubCategoria.setVisible(false);
            txtSubcategoria.setVisible(false);
        }
    }

    private void CargarPresupuesto(){
        Presupuesto presupuesto = ControladorPresupuesto.getInstancia().getPresupuesto();
        StringBinding textoPrespupuesto = Bindings.createStringBinding(() -> String.format("Q. %.2f", presupuesto.montoProperty().get()), presupuesto.montoProperty());
        txtPresupuesto.textProperty().bind(textoPrespupuesto);
    }

    private void CargarComboBoxTipoMovimiento(){
        cbMovimiento.getItems().add("Ingreso");
        cbMovimiento.getItems().add("Egreso");
        cbMovimiento.setValue("Ingreso");
        cbMovimiento.setStyle("-fx-text-fill: white");
    }

    private void CargarRecurrente(){
        txtDias.visibleProperty().bind(cbRecurrente.selectedProperty());
        labelDias.visibleProperty().bind(cbRecurrente.selectedProperty());
    }

    private void CargarComboBoxCategoria(){
        ObservableList<Categoria> categorias = ManejadorCategoria.getCategorias();
        cbCategoria.itemsProperty().bind(Bindings.createObjectBinding(() -> categorias, categorias));
        cbCategoria.setCellFactory(listView -> new CategoriaCell());
        cbCategoria.setButtonCell(new CategoriaCell());
    }
}
