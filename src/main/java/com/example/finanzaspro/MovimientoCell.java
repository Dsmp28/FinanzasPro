package com.example.finanzaspro;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MovimientoCell extends ListCell<Movimiento> {
    private HBox content;
    private ImageView imageView;
    private Text titulo;
    private Text categoria;
    private Text cantidad;
    private Text fecha;

    public MovimientoCell() {
        super();
        imageView = new ImageView();
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);

        titulo = new Text();
        titulo.setStyle("-fx-font-weight: bold");

        categoria = new Text();

        cantidad = new Text();

        fecha = new Text();
        fecha.setStyle("-fx-fill: '#55595f'; -fx-font-size: 10px");

        VBox vBox = new VBox(cantidad, fecha);
        vBox.setSpacing(5);
        vBox.alignmentProperty().setValue(javafx.geometry.Pos.CENTER_RIGHT);

        VBox vBoxTituloCategoria = new VBox(titulo, categoria);
        vBoxTituloCategoria.setSpacing(5);

        content = new HBox(imageView, vBoxTituloCategoria, vBox);
        content.setHgrow(vBox, javafx.scene.layout.Priority.ALWAYS);
        content.setSpacing(10); // Espacio entre la imagen y el texto
        content.setPadding(new javafx.geometry.Insets(5));
        content.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Movimiento item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            imageView.setImage(item.getCategoria().getImagen());
            titulo.setText(item.getTitulo());
            categoria.setText(item.getCategoria().getTitulo());
            cantidad.setText(String.format("Q. %.2f", item.getCantidad()));

            if (item.getCantidad() < 0) {
                cantidad.setStyle("-fx-fill: 'red'; -fx-font-weight: bold; -fx-font-size: 14px");
            } else {
                cantidad.setStyle("-fx-fill: 'green'; -fx-font-weight: bold; -fx-font-size:14px");
            }
            fecha.setText(item.getFecha().toString());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
