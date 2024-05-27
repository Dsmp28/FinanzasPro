package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
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

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtCantidad;

    @FXML
    private DatePicker dpFecha;



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

    @FXML
    public void AgregarMovimiento(){
        if (ValidarDatosObligatorios()){
            try{
                Categoria categoriaSeleccionada = cbCategoria.getSelectionModel().getSelectedItem();
                if (categoriaSeleccionada.getTitulo().equals("Otra")){
                    String tituloNuevaCategoria = txtSubcategoria.getText();
                    ManejadorCategoria.agregarCategoria(tituloNuevaCategoria);
                    categoriaSeleccionada = ManejadorCategoria.buscarCategoriaPorTitulo(tituloNuevaCategoria);
                }
                String tipo = cbMovimiento.getValue();
                String titulo = txtTitulo.getText();

                double cantidad = Double.parseDouble(txtCantidad.getText());
                if (tipo.equals("Egreso")){
                    cantidad = cantidad * -1;
                }

                boolean esRecurrente = cbRecurrente.isSelected();
                int intervaloDias = 0;
                if (esRecurrente){
                    intervaloDias = Integer.parseInt(txtDias.getText());
                    if (intervaloDias <= 0){
                        throw new IllegalArgumentException("El intervalo de días debe ser mayor a 0");
                    }
                }
                LocalDate fecha = dpFecha.getValue();

                ManejadorMovimiento.agregarMovimiento(categoriaSeleccionada, tipo, titulo, cantidad, fecha, esRecurrente, intervaloDias);
                ManejadorAlertas.showInformation("Movimiento agregado", "Movimiento agregado exitosamente", "El movimiento ha sido agregado exitosamente");
                limpiarCampos();
            }catch (Exception e){
                ManejadorAlertas.showError("Error", "Error al agregar movimiento", e.getMessage());
            }
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

    private boolean ValidarDatosObligatorios(){
        if (txtTitulo.getText().isEmpty() || txtCantidad.getText().isEmpty() || dpFecha.getValue() == null || cbCategoria.getSelectionModel().getSelectedItem() == null){
            ManejadorAlertas.showError("Error", "Faltan datos obligatorios", "Por favor, llene todos los campos obligatorios");
            return false;
        }else {
            try {
                if (Double.parseDouble(txtCantidad.getText()) <= 0){
                    ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número mayor a 0 en el campo de cantidad");
                    return false;
                }
            } catch (NumberFormatException e) {
                ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número válido en el campo de cantidad");
                return false;
            }
        }
        return true;
    }

    private void limpiarCampos(){
        txtTitulo.setText("");
        txtCantidad.setText("");
        dpFecha.setValue(null);
        cbCategoria.getSelectionModel().clearSelection();
        cbMovimiento.setValue("Ingreso");
        cbRecurrente.setSelected(false);
        txtDias.setText("");
        txtSubcategoria.setText("");
    }
}
