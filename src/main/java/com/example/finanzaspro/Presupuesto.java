package com.example.finanzaspro;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Presupuesto {

    private final DoubleProperty monto;

    public Presupuesto(double monto) {
        this.monto = new SimpleDoubleProperty(monto);
    }

    public void modificarCantidad(double monto) {
        ManejadorEncriptacion.guardarPresupuestoEnJSON((this.monto.get() + monto), "DatoPresupuesto.json");
        this.monto.set(this.monto.get() + monto);
    }

    public void setMonto(double monto) {
        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "DatoPresupuesto.json");
        this.monto.set(monto);
    }

    public DoubleProperty montoProperty() {
        return monto;
    }
}
