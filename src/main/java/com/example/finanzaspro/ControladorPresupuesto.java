package com.example.finanzaspro;

public class ControladorPresupuesto {
    private static ControladorPresupuesto instancia;
    private final Presupuesto presupuesto;

    private ControladorPresupuesto() {
        double presupuestoOriginal = ManejadorEncriptacion.leerPresupuestoDeJSON("PresupuestoOriginal.json");
        presupuesto = new Presupuesto(presupuestoOriginal + calcularMontoTotal());
    }

    public void actualizarPresupuesto() {
        double cantidad = calcularMontoTotal();
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

    private static double calcularMontoTotal() {
        double montoTotal = 0.0;
        montoTotal += ManejadorEncriptacion.leerPresupuestoDeJSON("DatoIngresos.json");
        montoTotal += ManejadorEncriptacion.leerPresupuestoDeJSON("DatoEgresos.json");
        return montoTotal;
    }

}
