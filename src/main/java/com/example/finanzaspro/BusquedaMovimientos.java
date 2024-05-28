package com.example.finanzaspro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BusquedaMovimientos {
    private final ObservableList<Movimiento> movimientos;
    private final Categoria categoriaElegida;
    private String tipo;
    private final String titulo;

    public BusquedaMovimientos(ObservableList<Movimiento> movimientos, Categoria categoriaElegida, String tipo, String titulo) {
        this.movimientos = movimientos;
        this.categoriaElegida = categoriaElegida;
        this.tipo = tipo;
        this.titulo = titulo;
    }

    public BusquedaMovimientos() {
        this.movimientos = ManejadorMovimiento.getMovimientos();
        this.categoriaElegida = null;
        this.tipo = "";
        this.titulo = "";
    }

    public ObservableList<Movimiento> BuscarMovimientos(){
        ObservableList<Movimiento> movimientosFiltrados = FXCollections.observableArrayList();

        for (Movimiento movimiento : movimientos) {
            if (filtrarPorTipo(movimiento) && filtrarPorCategoria(movimiento) && filtrarPorTitulo(movimiento)) {
                movimientosFiltrados.add(movimiento);
            }
        }

        return movimientosFiltrados;
    }

    public ObservableList<Movimiento> BuscarMovimientos(String tipo){
        ObservableList<Movimiento> movimientosFiltrados = FXCollections.observableArrayList();
        this.tipo = tipo;
        for (Movimiento movimiento : movimientos) {
            if (filtrarPorTipo(movimiento)) {
                movimientosFiltrados.add(movimiento);
            }
        }

        return movimientosFiltrados;
    }

    private boolean filtrarPorTipo(Movimiento movimiento) {
        return tipo.equals("Todos") || movimiento.getTipo().equals(tipo);
    }

    private boolean filtrarPorCategoria(Movimiento movimiento) {
        return categoriaElegida == null ||categoriaElegida.getTitulo().equals("Todas las categorías") || movimiento.getCategoria().equals(categoriaElegida);
    }

    private boolean filtrarPorTitulo(Movimiento movimiento) {
        return titulo.isEmpty() || movimiento.getTitulo().toLowerCase().contains(titulo.toLowerCase());
    }
}

