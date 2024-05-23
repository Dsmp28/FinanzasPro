package com.example.finanzaspro;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;

public class Ingreso {

    private DoubleProperty monto;

    public Ingreso(double monto) {
        this.monto = new SimpleDoubleProperty(monto);
        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "DatoIngresos.json");
    }

    public void modificarCantidad(double monto) {
        ManejadorEncriptacion.guardarPresupuestoEnJSON((this.monto.get() + monto), "DatoIngresos.json");
        this.monto.set(this.monto.get() + monto);
    }

    public void setMonto(double monto) {
        ManejadorEncriptacion.guardarPresupuestoEnJSON(monto, "DatoIngresos.json");
        this.monto.set(monto);
    }

    public DoubleProperty montoProperty() {
        return monto;
    }
}
