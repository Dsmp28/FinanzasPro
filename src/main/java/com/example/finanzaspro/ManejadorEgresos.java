package com.example.finanzaspro;

public class ManejadorEgresos {
    private static ManejadorEgresos instancia;
    private Egreso egreso;

    private ManejadorEgresos() {
        double cantidad = ManejadorEncriptacion.leerPresupuestoDeJSON("DatoEgresos.json");
        egreso = new Egreso(cantidad);
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
}
