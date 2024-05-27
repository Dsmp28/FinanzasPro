package com.example.finanzaspro;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecordatorioCell extends ListCell<Movimiento> {
    private HBox content;
    private ImageView imagen;
    private Text titulo;
    private Text categoria;
    private Text cantidad;
    private Text diasRestantes;

    public RecordatorioCell() {
        super();

        imagen = new ImageView();
        imagen.setFitHeight(20);
        imagen.setFitWidth(20);
        titulo = new Text();
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 10px");

        categoria = new Text();

        cantidad = new Text();
        cantidad.setStyle("-fx-font-size: 8px");

        diasRestantes = new Text();
        diasRestantes.setStyle("-fx-fill: '#55595f'; -fx-font-size: 10px");

        HBox hBox = new HBox(titulo, cantidad, diasRestantes);
        hBox.setSpacing(35);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        content = new HBox(imagen, hBox);
        content.setSpacing(10);
        content.setPadding(new javafx.geometry.Insets(5));
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Movimiento item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            imagen.setImage(item.getCategoria().getImagen());
            titulo.setText(item.getTitulo());
            categoria.setText(item.getCategoria().getTitulo());
            cantidad.setText(String.format("Q. %.2f", item.getCantidad()));
            diasRestantes.setText(String.format("%d días restantes", item.calcularDiasRestantes()));

            if (item.getCantidad() < 0) {
                cantidad.setStyle("-fx-fill: 'red'; -fx-font-weight: bold; -fx-font-size: 10px");
            } else {
                cantidad.setStyle("-fx-fill: 'green'; -fx-font-weight: bold; -fx-font-size:10px");
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
