package com.example.finanzaspro;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class dashboardC implements Initializable, paneController{

    private DashboardController dashboardController;
    private ObservableList<Inversion> inversiones;
    private static int currentIndexInversiones;

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

    @FXML
    private ListView lvListaRecordatorios;

    @FXML
    private Button btnCompleto;

    @FXML
    private Button btnVerInv;

    @FXML
    private Label lbTituloInversion;

    @FXML
    private Label lbPorcentajeInversion;

    @FXML
    private ProgressBar pbBarraProgreso;

    @FXML
    private Label lbMesesRestantesInversion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inversiones = ManejadorInversion.getInversiones();
        currentIndexInversiones = inversiones.size() - 1;

        btnCompleto();
        MostrarTodosMovimientos();
        CargarIngresado();
        CargarEgresos();
        CargarPresupuesto();
        CargarRecordatorios();
        CargarInversiones();

        lvListaMovimientos.setOnMouseClicked(this::listViewDoubleClick);
    }

    @FXML
    public void MostrarTodosMovimientos(){
        botonActivo(btnTodos);
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        AsignarListaMovimientos(movimientos);
    }

    @FXML
    public void MostrarGastos(){
        botonActivo(btnGastos);
        BusquedaMovimientos BusquedaMovimientos = new BusquedaMovimientos();

        ObservableList<Movimiento> movimientosEncontrados = BusquedaMovimientos.BuscarMovimientos("Egreso");

        AsignarListaMovimientos(movimientosEncontrados);
    }

    @FXML
    public void MostrarIngresos(){
        botonActivo(btnIngresos);
        BusquedaMovimientos BusquedaMovimientos = new BusquedaMovimientos();

        ObservableList<Movimiento> movimientosEncontrados = BusquedaMovimientos.BuscarMovimientos("Ingreso");

        AsignarListaMovimientos(movimientosEncontrados);
    }

    @FXML
    private void siguienteInversion(){
        if (currentIndexInversiones < inversiones.size() - 1) {
            currentIndexInversiones++;
            mostrarInversion();
        }
    }

    @FXML
    private void anteriorInversion(){
        if (currentIndexInversiones > 0) {
            currentIndexInversiones--;
            mostrarInversion();
        }
    }

    private void CargarInversiones(){
        if (inversiones.isEmpty()){
            lbTituloInversion.setText("No hay inversiones");
            lbPorcentajeInversion.setText("0% alcanzado");
            pbBarraProgreso.setProgress(0);
            lbMesesRestantesInversion.setText("No hay meses restantes");
        }else {
            mostrarInversion();
        }
    }

    private void mostrarInversion(){
        Inversion inversion = inversiones.get(currentIndexInversiones);
        lbTituloInversion.setText(inversion.getNombre());
        int porcentaje = (int) Math.round(inversion.getValorActual() / inversion.getMontoMeta() * 100);
        lbPorcentajeInversion.setText(porcentaje + "% alcanzado");
        pbBarraProgreso.setProgress(inversion.getValorActual() / inversion.getMontoMeta());
        lbMesesRestantesInversion.setText((inversion.getPlazoMeses() - inversion.getAbonosMensuales().size()) + " meses restantes");
    }

    private void CargarRecordatorios(){
        ObservableList<Movimiento> recordatorios = ManejadorMovimiento.getRecordatorios();
        Collections.sort(recordatorios, Comparator.comparing(Movimiento::calcularDiasRestantes));
        lvListaRecordatorios.itemsProperty().bind(Bindings.createObjectBinding(() -> recordatorios, recordatorios));
        lvListaRecordatorios.setCellFactory(listView -> new RecordatorioCell());
    }

    private void AsignarListaMovimientos(ObservableList<Movimiento> movimientos){
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
            emergente.initStyle(javafx.stage.StageStyle.UTILITY);
            emergente.setTitle("Editar movimiento");
            emergente.setScene(new Scene(root));
            editarController controller = loader.getController();
            controller.setStage(emergente);
            controller.setMovimiento(movimiento);
            emergente.showAndWait();

            MostrarTodosMovimientos();
            CargarIngresado();
            CargarEgresos();
            CargarPresupuesto();
            CargarRecordatorios();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void ValidarEnviarCorreo(){
        ObservableList<Movimiento> recordatorios = ManejadorMovimiento.getRecordatorios();
        boolean enviarCorreo = false;

        for (Movimiento movimiento : recordatorios) {
            long diasRestantes = movimiento.calcularDiasRestantes();
            if (diasRestantes < 3) {
                enviarCorreo = true;
                break;
            }
        }

        if (enviarCorreo) {
            enviarRecordatoriosPorCorreo(recordatorios);
        }
    }

    private void enviarRecordatoriosPorCorreo(ObservableList<Movimiento> recordatorios) {
        StringBuilder cuerpoCorreo = new StringBuilder("Movimientos que faltan menos de 3 días para suceder:\n\n");

        for (Movimiento movimiento : recordatorios) {
            long diasRestantes = movimiento.calcularDiasRestantes();
            if (diasRestantes < 3) {
                cuerpoCorreo.append("Movimiento: ").append(movimiento.getTitulo())
                        .append(", Días restantes: ").append(diasRestantes).append("\n");
            }
        }

        // Enviar el correo
        SendGridEmailService emailService = new SendGridEmailService();
        try {
            emailService.sendEmail("Recordatorios de Movimientos", cuerpoCorreo.toString());
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public void btnCompleto(){
        btnCompleto.setOnAction(event -> {
            if(dashboardController != null){
                dashboardController.cambiarVista("movimientos-view.fxml", dashboardController.getBtnTransaccion());
            }
        });
    }
    @Override
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
    public void btnVerInv(){
        btnVerInv.setOnAction(event -> {
            if(dashboardController != null){
                dashboardController.cambiarVista("inversion-view.fxml", dashboardController.getBtnInversion());
            }
        });
    }
}
