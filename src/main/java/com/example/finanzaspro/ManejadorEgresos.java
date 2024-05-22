package com.example.finanzaspro;

import javafx.collections.ObservableList;

public class ManejadorEgresos {
    private static ManejadorEgresos instancia;
    private Egreso egreso;

    private ManejadorEgresos() {
        ObservableList<Categoria> categorias = ManejadorCategoria.getCategorias();
        ObservableList<Movimiento> movimientos = ManejadorEncriptacion.leerMovimientosDeJSON("DatosMovimientos.json", categorias);
        double cantidad = calcularMontoTotalEgresos(movimientos);
        egreso = new Egreso(cantidad);
        ManejadorEncriptacion.guardarPresupuestoEnJSON(cantidad, "DatoEgresos.json");
    }

    public static synchronized ManejadorEgresos getInstancia() {
        if (instancia == null) {
            instancia = new ManejadorEgresos();
        }
        return instancia;
    }

    public Egreso getEgreso() {
        return egreso;
    }

    private static double calcularMontoTotalEgresos(ObservableList<Movimiento> movimientos) {
        double montoTotalEgresos = 0.0;

        for (Movimiento movimiento : movimientos) {
            if ("Egreso".equals(movimiento.getTipo())) {
                montoTotalEgresos += movimiento.getCantidad();
            }
        }

        return montoTotalEgresos;
    }
}
