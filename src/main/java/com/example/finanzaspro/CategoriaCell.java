package com.example.finanzaspro;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CategoriaCell extends ListCell<Categoria> {
    private final HBox content;
    private final Text name;
    private final ImageView imageView;

    public CategoriaCell() {
        super();
        name = new Text();
        imageView = new ImageView();
        content = new HBox(imageView, name);
        content.setSpacing(10); // Espacio entre la imagen y el texto
    }

    @Override
    protected void updateItem(Categoria item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            name.setText(item.getTitulo());
            imageView.setImage(item.getImagen());
            imageView.setFitHeight(20); // Ajustar el tamaño de la imagen
            imageView.setFitWidth(20);
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

