package com.example.finanzaspro;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.plaf.synth.Region;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class estadisticasController implements Initializable {

    private ObservableList<Movimiento> movimientos;

    @FXML
    private TableView tvDatos;

    @FXML
    private ScatterChart chartDispersion;

    @FXML
    private BarChart chartBarras;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movimientos = ManejadorMovimiento.getMovimientos();
        CargarTabla();
        cargarDatosEnScatterChart();
        cargarDatosEnBarChart();
    }
    @FXML
    public void handleExportar(ActionEvent event){
        List<String> choices = List.of("Descargar", "Enviar por correo");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Descargar", choices);
        dialog.setTitle("Exportar Datos");
        dialog.setHeaderText("Elige una opción");
        dialog.setContentText("Seleccione:");
        dialog.setGraphic(null);

        Stage alertStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResource("icons/cuota.png").toString()));
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("cssestadisticas.css").toExternalForm());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            if (selected.equals("Descargar")) {
                guardarCsvEnRutaSeleccionada();
            } else {
                //código de enviar por correo
            }
        });
    }

    public void guardarCsvEnRutaSeleccionada() {
        // Crear un FileChooser
        FileChooser fileChooser = new FileChooser();

        // Configurar el FileChooser
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Mostrar el diálogo de guardar archivo
        File file = fileChooser.showSaveDialog(null);

        // Si el usuario seleccionó un archivo
        if (file != null) {
            // Instanciar SendGridEmailService
            SendGridEmailService emailService = new SendGridEmailService();

            // Generar el archivo CSV
            String csvPath = emailService.convertMovimientosToCsv(movimientos);

            try {
                // Mover el archivo CSV a la ubicación seleccionada
                Files.move(Paths.get(csvPath), file.toPath());
            } catch (IOException e) {
                // Manejar la excepción
                System.err.println("Error al mover el archivo CSV: " + e.getMessage());
            }
        }
    }

    private void CargarTabla(){
        // Crear las columnas
        TableColumn<Movimiento, String> categoriaColumn = new TableColumn<>("Categoría");
        categoriaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria().getTitulo()));

        TableColumn<Movimiento, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));

        TableColumn<Movimiento, String> tipoColumn = new TableColumn<>("Tipo");
        tipoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo()));

        TableColumn<Movimiento, Double> cantidadColumn = new TableColumn<>("Cantidad");
        cantidadColumn.setCellValueFactory(cellData -> {
            double cantidad = cellData.getValue().getCantidad();
            if (cantidad < 0) {
                cantidad *= -1;
            }
            return new SimpleObjectProperty<>(cantidad);
        });

        TableColumn<Movimiento, LocalDate> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFecha()));

        TableColumn<Movimiento, Boolean> esRecurrenteColumn = new TableColumn<>("Es Recurrente");
        esRecurrenteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isEsRecurrente()));

        TableColumn<Movimiento, Integer> intervaloDiasColumn = new TableColumn<>("Intervalo de Días");
        intervaloDiasColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIntervaloDias()));

        // Añadir las columnas al TableView
        tvDatos.getColumns().add(categoriaColumn);
        tvDatos.getColumns().add(tituloColumn);
        tvDatos.getColumns().add(tipoColumn);
        tvDatos.getColumns().add(cantidadColumn);
        tvDatos.getColumns().add(fechaColumn);
        tvDatos.getColumns().add(esRecurrenteColumn);
        tvDatos.getColumns().add(intervaloDiasColumn);

        tvDatos.setItems(movimientos);
    }



    public void cargarDatosEnScatterChart() {
        chartDispersion.getXAxis().setLabel("Fecha");
        chartDispersion.getYAxis().setLabel("Cantidad");

        XYChart.Series<String, Number> seriesGastos = new XYChart.Series<>();
        seriesGastos.setName("Gastos");
        XYChart.Series<String, Number> seriesIngresos = new XYChart.Series<>();
        seriesIngresos.setName("Ingresos");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

        // Convertir los datos de la serie a una lista
        List<XYChart.Data<String, Number>> dataListGastos = new ArrayList<>();
        List<XYChart.Data<String, Number>> dataListIngresos = new ArrayList<>();
        for (Movimiento movimiento : movimientos) {
            // Convertir LocalDate a String
            String fechaString = movimiento.getFecha().format(formatter);
            if (movimiento.getTipo().equals("Egreso")) {
                dataListGastos.add(new XYChart.Data<>(fechaString, movimiento.getCantidad()));
            } else if (movimiento.getTipo().equals("Ingreso")) {
                dataListIngresos.add(new XYChart.Data<>(fechaString, movimiento.getCantidad()));
            }
        }

        // Ordenar las listas por fecha
        Comparator<XYChart.Data<String, Number>> comparator = Comparator.comparing(data -> LocalDate.parse(data.getXValue(), formatter));
        Collections.sort(dataListGastos, comparator);
        Collections.sort(dataListIngresos, comparator);

        // Agregar los datos ordenados a las series
        seriesGastos.getData().addAll(dataListGastos);
        seriesIngresos.getData().addAll(dataListIngresos);

        chartDispersion.getData().add(seriesGastos);
        chartDispersion.getData().add(seriesIngresos);

        // Asignar el estilo directamente a los nodos de las series
        for (XYChart.Data<String, Number> data : seriesGastos.getData()) {
            Node node = data.getNode();
            node.setStyle("-fx-shape: 'M0,0 L0,8 L8,4 Z'; -fx-background-color: red;");
        }
        for (XYChart.Data<String, Number> data : seriesIngresos.getData()) {
            Node node = data.getNode();
            node.setStyle("-fx-shape: 'M0,0 L8,0 L8,8 L0,8 Z'; -fx-background-color: green;");
        }

        // Asignar el estilo a las leyendas
        for (Node legendItem : chartDispersion.lookupAll(".chart-legend-item")) {
            if (legendItem instanceof Label) {
                Label legendLabel = (Label) legendItem;
                if (legendLabel.getText().equals("Gastos")) {
                    legendLabel.setGraphic(createLegendShape("triangle", javafx.scene.paint.Color.RED));
                } else if (legendLabel.getText().equals("Ingresos")) {
                    legendLabel.setGraphic(createLegendShape("square", javafx.scene.paint.Color.GREEN));
                }
            }
        }
    }


    private Node createLegendShape(String shape, javafx.scene.paint.Color color) {
        Node shapeNode;
        switch (shape) {
            case "triangle":
                javafx.scene.shape.Polygon triangle = new javafx.scene.shape.Polygon();
                triangle.getPoints().addAll(0.0, 0.0, 0.0, 8.0, 8.0, 4.0);
                triangle.setFill(color);
                shapeNode = triangle;
                break;
            case "square":
                javafx.scene.shape.Rectangle square = new javafx.scene.shape.Rectangle(8, 8);
                square.setFill(color);
                shapeNode = square;
                break;
            default:
                throw new IllegalArgumentException("Forma desconocida: " + shape);
        }
        return shapeNode;
    }

    public void cargarDatosEnBarChart() {
        chartBarras.getXAxis().setLabel("Categoría");
        chartBarras.getYAxis().setLabel("Cantidad");

        XYChart.Series<String, Number> seriesGastos = new XYChart.Series<>();
        seriesGastos.setName("Gastos");
        XYChart.Series<String, Number> seriesIngresos = new XYChart.Series<>();
        seriesIngresos.setName("Ingresos");

        // Agrupar los movimientos por categoría y tipo
        Map<String, Double> gastosPorCategoria = new HashMap<>();
        Map<String, Double> ingresosPorCategoria = new HashMap<>();
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals("Egreso")) {
                gastosPorCategoria.merge(movimiento.getCategoria().getTitulo(), movimiento.getCantidad(), Double::sum);
            } else if (movimiento.getTipo().equals("Ingreso")) {
                ingresosPorCategoria.merge(movimiento.getCategoria().getTitulo(), movimiento.getCantidad(), Double::sum);
            }
        }

        // Agregar los datos a las series
        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            seriesGastos.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, Double> entry : ingresosPorCategoria.entrySet()) {
            seriesIngresos.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chartBarras.getData().add(seriesGastos);
        chartBarras.getData().add(seriesIngresos);
    }
}
