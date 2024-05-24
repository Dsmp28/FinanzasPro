package com.example.finanzaspro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ManejadorInversion {

    private static final ObservableList<Inversion> inversiones = FXCollections.observableArrayList();

    static {
        // Inicializar la lista de categorías desde el archivo JSON
        leerInversionesDesdeArchivo();
    }

    public static ObservableList<Inversion> getInversiones() {
        return inversiones;
    }

    public static void agregarInversion(double montoMeta, double tasaRetorno, int plazoMeses) {
        Inversion nuevaInversion = new Inversion(montoMeta, tasaRetorno, plazoMeses);
        inversiones.add(nuevaInversion);
        guardarInversionesEnArchivo();
    }

    public static void agregarAbono(int index, double abono) {
        Inversion inversion = inversiones.get(index);
        inversion.agregarAbono(abono);
        guardarInversionesEnArchivo();
    }

    private static void leerInversionesDesdeArchivo() {
        ObservableList<Inversion> inversionesDesdeArchivo = ManejadorEncriptacion.leerInversionesDeJSON("DatoInversiones.json");
        inversiones.setAll(inversionesDesdeArchivo);
    }

    private static void guardarInversionesEnArchivo() {
        ManejadorEncriptacion.guardarInversionEnJson(inversiones, "DatoInversiones.json");
    }
}
