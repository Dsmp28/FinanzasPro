package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class editarController {

    @FXML
    private ChoiceBox<String> cbMovimiento;

    @FXML
    private CheckBox cbRecurrente;

    @FXML
    private Label labelDias;

    @FXML
    private TextField txtDias;

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private Label lblSubCategoria;

    @FXML
    private TextField txtSubcategoria;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtCantidad;

    @FXML
    private DatePicker dpFecha;

    private Stage stage;
    private Movimiento movimiento;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
        llenarCampos();
    }

    private void llenarCampos(){
        CargarComboBoxCategoria();
        CargarComboBoxTipoMovimiento();
        txtTitulo.setText(movimiento.getTitulo());
        txtCantidad.setText(String.valueOf(movimiento.getCantidad() < 0 ? movimiento.getCantidad() * -1 : movimiento.getCantidad()));
        dpFecha.setValue(movimiento.getFecha());
        cbRecurrente.setSelected(movimiento.isEsRecurrente());
        if(cbRecurrente.isSelected()){
            txtDias.setVisible(true);
            labelDias.setVisible(true);
        }else{
            txtDias.setVisible(false);
            labelDias.setVisible(false);
        }
        txtDias.setText(String.valueOf(movimiento.getIntervaloDias()));
        cbCategoria.setValue(movimiento.getCategoria());
    }

    @FXML
    private void toggleRecurrente(){
        txtDias.setVisible(cbRecurrente.isSelected());
        labelDias.setVisible(cbRecurrente.isSelected());
    }

    @FXML
    public void eliminarMovimiento(){
        ManejadorMovimiento.getMovimientos().remove(movimiento);

        ManejadorEncriptacion.guardarMovimientosEnJSON(ManejadorMovimiento.getMovimientos(), "DatosMovimientos.json");

        ManejadorIngreso.getInstancia().actualizarIngresos();
        ManejadorEgresos.getInstancia().actualizarEgresos();
        ControladorPresupuesto.getInstancia().actualizarPresupuesto();

        ManejadorAlertas.showInformation("Movimiento eliminado", "Movimiento eliminado exitosamente", "El movimiento ha sido editado exitosamente");
        stage.close();
    }

    @FXML
    private void guardarMovimiento(){
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
                    if (intervaloDias <= 0 || intervaloDias > 999999999){
                        throw new IllegalArgumentException("El intervalo de días debe ser mayor a 0 y menor a 999,999,999");
                    }
                }
                LocalDate fecha = dpFecha.getValue();

                // Actualizar el movimiento actual
                movimiento.setCategoria(categoriaSeleccionada);
                movimiento.setTipo(tipo);
                movimiento.setTitulo(titulo);
                movimiento.setCantidad(cantidad);
                movimiento.setEsRecurrente(esRecurrente);
                movimiento.setIntervaloDias(intervaloDias);
                movimiento.setFecha(fecha);


                ManejadorEncriptacion.guardarMovimientosEnJSON(ManejadorMovimiento.getMovimientos(), "DatosMovimientos.json");
                ManejadorIngreso.getInstancia().actualizarIngresos();
                ManejadorEgresos.getInstancia().actualizarEgresos();
                ControladorPresupuesto.getInstancia().actualizarPresupuesto();

                ManejadorAlertas.showInformation("Movimiento editado", "Movimiento editado exitosamente", "El movimiento ha sido editado exitosamente");
                stage.close();
            }catch (Exception e){
                ManejadorAlertas.showError("Error", "Error al agregar movimiento", e.getMessage());
            }
        }
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

    private boolean ValidarDatosObligatorios(){
        if (txtTitulo.getText().isEmpty() || txtCantidad.getText().isEmpty() || dpFecha.getValue() == null || cbCategoria.getSelectionModel().getSelectedItem() == null){
            ManejadorAlertas.showError("Error", "Faltan datos obligatorios", "Por favor, llene todos los campos obligatorios");
            return false;
        }else {
            try {
                if (Double.parseDouble(txtCantidad.getText()) <= 0 || Double.parseDouble(txtCantidad.getText()) > 999999999999999.0){
                    ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número mayor a 0 y menor a 999,999,999,999,999 en el campo de cantidad");
                    return false;
                }
            } catch (NumberFormatException e) {
                ManejadorAlertas.showError("Error", "Cantidad inválida", "Por favor, ingrese un número válido en el campo de cantidad");
                return false;
            }
        }
        return true;
    }

    @FXML
    private void cerrar(){
        stage.close();
    }

    private void CargarComboBoxTipoMovimiento(){
        cbMovimiento.getItems().add("Ingreso");
        cbMovimiento.getItems().add("Egreso");
        cbMovimiento.setValue(movimiento.getTipo());
        cbMovimiento.setStyle("-fx-text-fill: white");
    }
    private void CargarComboBoxCategoria(){
        ObservableList<Categoria> categorias = ManejadorCategoria.getCategorias();
        cbCategoria.itemsProperty().bind(Bindings.createObjectBinding(() -> categorias, categorias));
        cbCategoria.setCellFactory(listView -> new CategoriaCell());
        cbCategoria.setButtonCell(new CategoriaCell());
    }
}
