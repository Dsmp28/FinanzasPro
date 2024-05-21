package com.example.finanzaspro;

public class ControladorPresupuesto {
    private static ControladorPresupuesto instancia;
    private Presupuesto presupuesto;

    private ControladorPresupuesto() {
        double cantidad = ManejadorEncriptacion.leerPresupuestoDeJSON("DatoPresupuesto.json");
        presupuesto = new Presupuesto(cantidad);
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
}
