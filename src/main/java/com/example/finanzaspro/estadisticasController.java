package com.example.finanzaspro;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
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
    public void handleExportar() {
        List<String> choices = List.of("Descargar", "Enviar por correo");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Descargar", choices);
        dialog.setTitle("Exportar Datos");
        dialog.setHeaderText("Elige una opción");
        dialog.setContentText("Seleccione:");
        dialog.setGraphic(null);

        Stage alertStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/cuota.png")).toString()));
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("cssestadisticas.css")).toExternalForm());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            if (selected.equals("Descargar")) {
                guardarCsvEnRutaSeleccionada();
            } else {
                try {
                    SendGridEmailService emailService = new SendGridEmailService();

                    String message = """
                        Estimado/a,

                        Nos complace presentarle sus estadísticas financieras más recientes:""";

                    emailService.sendEmailWithAttachment("Estadísticas de movimientos", message, movimientos);
                    ManejadorAlertas.showInformation("Correo enviado", "Correo enviado con éxito", "El correo se ha enviado correctamente.\n\nRevise su bandeja de entrada o en su defecto su carpeta de spam.");

                } catch (Exception e) {
                    ManejadorAlertas.showError("Error al enviar el correo", "Ocurrió un error al enviar el correo", e.getMessage());
                }
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
            String csvContent = emailService.convertMovimientosToCsv(movimientos);

            try {
                // Escribir el contenido del CSV en el archivo seleccionado
                Files.write(file.toPath(), csvContent.getBytes());
                ManejadorAlertas.showConfirmation("Exportacion de archivos", "Archivo guardado con exito", "El archivo CSV se ha guardado correctamente en la ruta seleccionada.");
            } catch (IOException e) {
                // Manejar la excepción
                System.err.println("Error al escribir el archivo CSV: " + e.getMessage());
            }
        }
    }

    private void CargarTabla() {
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

        TableColumn<Movimiento, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yy"))));

        TableColumn<Movimiento, String> esRecurrenteColumn = new TableColumn<>("Es Recurrente");
        esRecurrenteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isEsRecurrente() ? "Sí" : "No"));

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
        dataListGastos.sort(comparator);
        dataListIngresos.sort(comparator);

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
            if (legendItem instanceof Label legendLabel) {
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
        XYChart.Series<String, Number> seriesTotal = new XYChart.Series<>();
        seriesTotal.setName("Total");

        // Agrupar los movimientos por categoría y tipo
        Map<String, Double> gastosPorCategoria = new HashMap<>();
        Map<String, Double> ingresosPorCategoria = new HashMap<>();
        Map<String, Double> totalPorCategoria = new HashMap<>();
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getTipo().equals("Egreso")) {
                double cantidad = movimiento.getCantidad();
                gastosPorCategoria.merge(movimiento.getCategoria().getTitulo(), cantidad, Double::sum);
                totalPorCategoria.merge(movimiento.getCategoria().getTitulo(), cantidad, Double::sum); // Gasto en valor negativo
            } else if (movimiento.getTipo().equals("Ingreso")) {
                double cantidad = movimiento.getCantidad();
                ingresosPorCategoria.merge(movimiento.getCategoria().getTitulo(), cantidad, Double::sum);
                totalPorCategoria.merge(movimiento.getCategoria().getTitulo(), cantidad, Double::sum);
            }
        }

        // Agregar los datos a las series
        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            data.nodeProperty().addListener((ov, oldNode, newNode) -> {
                if (newNode != null) {
                    instalarTooltipEnNodo(newNode, data.getXValue(), data.getYValue());
                }
            });
            seriesGastos.getData().add(data);
        }
        for (Map.Entry<String, Double> entry : ingresosPorCategoria.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            data.nodeProperty().addListener((ov, oldNode, newNode) -> {
                if (newNode != null) {
                    instalarTooltipEnNodo(newNode, data.getXValue(), data.getYValue());
                }
            });
            seriesIngresos.getData().add(data);
        }
        for (Map.Entry<String, Double> entry : totalPorCategoria.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            data.nodeProperty().addListener((ov, oldNode, newNode) -> {
                if (newNode != null) {
                    instalarTooltipEnNodo(newNode, data.getXValue(), data.getYValue());
                }
            });
            seriesTotal.getData().add(data);
        }

        chartBarras.getData().add(seriesGastos);
        chartBarras.getData().add(seriesIngresos);
        chartBarras.getData().add(seriesTotal);
    }

    private void instalarTooltipEnNodo(Node nodo, String xValue, Number yValue) {
        Tooltip tooltip = new Tooltip("Categoría: " + xValue + "\nTotal: " + yValue);
        tooltip.setShowDelay(Duration.seconds(0.1)); // Mostrar el Tooltip después de 0.1 segundos
        tooltip.setStyle("-fx-font-size: 13px;"); // Aplicar la clase de estilo al Tooltip
        Tooltip.install(nodo, tooltip);
    }
}
