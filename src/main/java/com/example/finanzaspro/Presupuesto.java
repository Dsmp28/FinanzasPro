package com.example.finanzaspro;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Presupuesto {

    private DoubleProperty cantidad;

    public Presupuesto(double cantidad) {
        this.cantidad = new SimpleDoubleProperty(cantidad);
    }

    public double getCantidad() {
        return cantidad.get();
    }

    public void modificarCantidad(double cantidad) {
        this.cantidad.set(this.cantidad.get() + cantidad);
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }
}
