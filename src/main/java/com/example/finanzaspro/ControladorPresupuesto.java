package com.example.finanzaspro;

import javafx.collections.ObservableList;

public class ControladorPresupuesto {
    private static ControladorPresupuesto instancia;
    private Presupuesto presupuesto;

    private ControladorPresupuesto() {
        ObservableList<Categoria> categorias = ManejadorCategoria.getCategorias();
        ObservableList<Movimiento> movimientos = ManejadorEncriptacion.leerMovimientosDeJSON("DatosMovimientos.json", categorias);
        double presupuestoOriginal = ManejadorEncriptacion.leerPresupuestoDeJSON("PresupuestoOriginal.json");
        presupuesto = new Presupuesto(presupuestoOriginal + calcularMontoTotal(movimientos));
    }

    public void actualizarPresupuesto() {
        ObservableList<Movimiento> movimientos = ManejadorEncriptacion.leerMovimientosDeJSON("DatosMovimientos.json", ManejadorCategoria.getCategorias());
        double cantidad = calcularMontoTotal(movimientos);
        double presupuestoOriginal = ManejadorEncriptacion.leerPresupuestoDeJSON("PresupuestoOriginal.json");
        presupuesto.setMonto(presupuestoOriginal + cantidad);
    }

    public static synchronized ControladorPresupuesto getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPresupuesto();
        }
        return instancia;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    private static double calcularMontoTotal(ObservableList<Movimiento> movimientos) {
        double montoTotal = 0.0;
        montoTotal += ManejadorEncriptacion.leerPresupuestoDeJSON("DatoIngresos.json");
        montoTotal += ManejadorEncriptacion.leerPresupuestoDeJSON("DatoEgresos.json");
        return montoTotal;
    }
}
