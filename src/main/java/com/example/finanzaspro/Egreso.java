package com.example.finanzaspro;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;

public class Egreso {

    private DoubleProperty monto;

    public Egreso(double monto) {
        this.monto = new SimpleDoubleProperty(monto);
        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "DatoEgresos.json");
    }

    public void modificarCantidad(double monto) {
        ManejadorEncriptacion.guardarPresupuestoEnJSON((this.monto.get() + monto), "DatoEgresos.json");
        this.monto.set(this.monto.get() + monto);
    }

    public void setMonto(double monto) {
        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "DatoEgresos.json");
        this.monto.set(monto);
    }

    public DoubleProperty montoProperty() {
        return monto;
    }
}
