package com.example.finanzaspro;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecordatorioCell extends ListCell<Movimiento> {
    private HBox content;
    private Text titulo;
    private Text categoria;
    private Text cantidad;
    private Text diasRestantes;

    public RecordatorioCell() {
        super();

        titulo = new Text();
        titulo.setStyle("-fx-font-weight: bold");

        categoria = new Text();

        cantidad = new Text();

        diasRestantes = new Text();
        diasRestantes.setStyle("-fx-fill: '#55595f'; -fx-font-size: 10px");

        VBox vBox = new VBox(cantidad, diasRestantes);
        vBox.setSpacing(5);
        vBox.alignmentProperty().setValue(javafx.geometry.Pos.CENTER_RIGHT);

        VBox vBoxTituloCategoria = new VBox(titulo, categoria);
        vBoxTituloCategoria.setSpacing(5);

        content = new HBox(vBoxTituloCategoria, vBox);
        content.setHgrow(vBox, javafx.scene.layout.Priority.ALWAYS);
        content.setSpacing(10); // Espacio entre la imagen y el texto
        content.setPadding(new javafx.geometry.Insets(5));
        content.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Movimiento item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            titulo.setText(item.getTitulo());
            categoria.setText(item.getCategoria().getTitulo());
            cantidad.setText(String.format("Q. %.2f", item.getCantidad()));
            diasRestantes.setText(String.format("%d días restantes", item.calcularDiasRestantes()));

            if (item.getCantidad() < 0) {
                cantidad.setStyle("-fx-fill: 'red'; -fx-font-weight: bold; -fx-font-size: 14px");
            } else {
                cantidad.setStyle("-fx-fill: 'green'; -fx-font-weight: bold; -fx-font-size:14px");
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
