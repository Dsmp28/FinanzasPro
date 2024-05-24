package com.example.finanzaspro;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Categoria {
    private static int ultimoId = 0;
    private int id;
    private StringProperty titulo;
    private Image imagen;

    public Categoria(String titulo, Image imagen) {
        this.id = ++ultimoId;
        this.titulo = new SimpleStringProperty(titulo);
        this.imagen = imagen;
    }

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

    public Image getImagen() {
        return imagen;
    }

    public String getDireccionImagen() {
        return imagen.getUrl();
    }

}

