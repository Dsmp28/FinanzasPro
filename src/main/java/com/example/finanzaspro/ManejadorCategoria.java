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
        if (buscarCategoriaPorTitulo(titulo) == null && !titulo.equals("Otra") && !titulo.isEmpty()) {
            Categoria nuevaCategoria = new Categoria(titulo, new Image(ManejadorCategoria.class.getResource("icons/ImagenPrueba.png").toExternalForm()));
            categorias.add(categorias.size() - 1, nuevaCategoria);
            guardarCategoriasEnArchivo();
        }else{
            throw new IllegalArgumentException("La categoria ya existe o no se puede agregar");
        }
    }

    private static void leerCategoriasDesdeArchivo() {
        ObservableList<Categoria> categoriasDesdeArchivo = ManejadorEncriptacion.leerCategoriasDeJSON("DatosCategorias.json");
        categorias.setAll(categoriasDesdeArchivo);
    }

    private static void guardarCategoriasEnArchivo() {
        ManejadorEncriptacion.guardarCategoriasEnJSON(categorias, "DatosCategorias.json");
    }

    public static Categoria buscarCategoriaPorTitulo(String titulo) {
        for (Categoria categoria : categorias) {
            if (categoria.getTitulo().equals(titulo)) {
                return categoria;
            }
        }
        return null;
    }
}
