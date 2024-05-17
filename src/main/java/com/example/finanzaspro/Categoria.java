package com.example.finanzaspro;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Categoria {
    private int id;
    private StringProperty titulo;
    private Image imagen;

    public Categoria(int id, String titulo, Image imagen) {
        this.id = id;
        this.titulo = new SimpleStringProperty(titulo);
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }
}

