package com.example.finanzaspro;

public class ManejadorIngreso {
    private static ManejadorIngreso instancia;
    private Ingreso ingreso;

    private ManejadorIngreso() {
        double cantidad = ManejadorEncriptacion.leerPresupuestoDeJSON("DatoIngresos.json");
        ingreso = new Ingreso(cantidad);
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
}
