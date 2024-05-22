package com.example.finanzaspro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import java.time.LocalDate;

public class ManejadorMovimiento {
    private static final ObservableList<Movimiento> movimientos = FXCollections.observableArrayList();


    static {
        // Inicializar la lista de categorías desde el archivo JSON
        leerMovimientosDesdeArchivo();
    }

    public static ObservableList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public static void agregarMovimiento(Categoria categoria, String tipo ,String titulo, double cantidad, LocalDate fecha, boolean esRecurrente, int intervaloDias) {
        Movimiento nuevoMovimiento = new Movimiento(categoria, tipo,titulo, cantidad, fecha, esRecurrente, intervaloDias);
        movimientos.add(nuevoMovimiento);
        guardarMovimientosEnArchivo();
        ControladorPresupuesto.getInstancia().getPresupuesto().modificarCantidad(nuevoMovimiento.getCantidad());
        if (nuevoMovimiento.getTipo().equals("Ingreso")) {
            ManejadorIngreso.getInstancia().getIngreso().modificarCantidad(nuevoMovimiento.getCantidad());
        } else {
            ManejadorEgresos.getInstancia().getEgreso().modificarCantidad(nuevoMovimiento.getCantidad());
        }
    }

    private static void leerMovimientosDesdeArchivo() {
        ObservableList<Movimiento> movimientosDesdeArchivo = ManejadorEncriptacion.leerMovimientosDeJSON("DatosMovimientos.json", ManejadorCategoria.getCategorias());
        movimientos.setAll(movimientosDesdeArchivo);
    }

    private static void guardarMovimientosEnArchivo() {
        ManejadorEncriptacion.guardarMovimientosEnJSON(movimientos, "DatosMovimientos.json");
    }
}
