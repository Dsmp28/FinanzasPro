package com.example.finanzaspro;

import javafx.collections.ObservableList;

public class ManejadorIngreso {
    private static ManejadorIngreso instancia;
    private Ingreso ingreso;

    private ManejadorIngreso() {
        ObservableList<Categoria> categorias = ManejadorCategoria.getCategorias();
        ObservableList<Movimiento> movimientos = ManejadorEncriptacion.leerMovimientosDeJSON("DatosMovimientos.json", categorias);
        double cantidad = calcularMontoTotalIngresos(movimientos);
        ingreso = new Ingreso(cantidad);
        ManejadorEncriptacion.guardarPresupuestoEnJSON(cantidad, "DatoIngresos.json");
    }

    public static synchronized ManejadorIngreso getInstancia() {
        if (instancia == null) {
            instancia = new ManejadorIngreso();
        }
        return instancia;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    private static double calcularMontoTotalIngresos(ObservableList<Movimiento> movimientos) {
        double montoTotalIngresos = 0.0;

        for (Movimiento movimiento : movimientos) {
            if ("Ingreso".equals(movimiento.getTipo())) {
                montoTotalIngresos += movimiento.getCantidad();
            }
        }

        return montoTotalIngresos;
    }
}
