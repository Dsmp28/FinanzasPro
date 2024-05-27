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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    @FXML
    public PieChart pieGastos;

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
        mostrarGastosPorCategoria();

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

    public void mostrarGastosPorCategoria() {
        ObservableList<Movimiento> movimientos = ManejadorMovimiento.getMovimientos();
        // Crear un mapa para almacenar la suma de gastos por categoría
        Map<String, Double> gastosPorCategoria = new HashMap<>();

        // Recorrer los movimientos y sumar los gastos por categoría
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals("Egreso")) {
                String categoria = movimiento.getCategoria().getTitulo();
                double cantidad = movimiento.getCantidad() * -1;
                gastosPorCategoria.put(categoria, gastosPorCategoria.getOrDefault(categoria, 0.0) + cantidad);
            }
        }

        // Crear los datos para el PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("No hay gastos", 1));
        }

        // Configurar y mostrar el PieChart
        pieGastos.setData(pieChartData);
        pieGastos.setLabelsVisible(false);
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
            emergente.getIcons().add(new Image(getClass().getResource("icons/finanzas.png").toString()));
            emergente.initStyle(javafx.stage.StageStyle.DECORATED);
            emergente.setResizable(false);
            emergente.setMaximized(false);
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
            mostrarGastosPorCategoria();

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
