package com.example.finanzaspro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class ManejadorCategoria {

    private static final ObservableList<Categoria> categorias = FXCollections.observableArrayList();

    static {
        // Inicializar la lista de categorías desde el archivo JSON
        leerCategoriasDesdeArchivo();
    }

    public static ObservableList<Categoria> getCategorias() {
        return categorias;
    }

    public static void agregarCategoria(String titulo) {
        Categoria nuevaCategoria = new Categoria(titulo, new Image(ManejadorCategoria.class.getResource("icons/ImagenPrueba.png").toExternalForm()));

        if (titulo.equals("Otra")) {
            categorias.add(nuevaCategoria);
        } else {
            categorias.add(categorias.size() - 1, nuevaCategoria); // Agregar antes de la última posición (que será "Otra")
        }

        guardarCategoriasEnArchivo();
    }

    private static void leerCategoriasDesdeArchivo() {
        ObservableList<Categoria> categoriasDesdeArchivo = ManejadorEncriptacion.leerCategoriasDeJSON("DatosCategorias.json");
        categorias.setAll(categoriasDesdeArchivo);
    }

    private static void guardarCategoriasEnArchivo() {
        ManejadorEncriptacion.guardarCategoriasEnJSON(categorias, "DatosCategorias.json");
    }
}
