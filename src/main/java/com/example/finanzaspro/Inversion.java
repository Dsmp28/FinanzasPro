package com.example.finanzaspro;

import java.util.ArrayList;
import java.util.List;

public class Inversion {
    private final double montoMeta;
    private final double tasaRetorno; // Tasa de retorno mensual
    private final int plazoMeses;
    private final List<Double> abonosMensuales;
    private double valorActual; // Nuevo campo para el valor actual
    private final String nombre;

    public Inversion(double montoMeta, double tasaRetorno, int plazoMeses, String nombre) {
        this.montoMeta = montoMeta;
        this.tasaRetorno = tasaRetorno;
        this.plazoMeses = plazoMeses;
        this.abonosMensuales = new ArrayList<>();
        this.nombre = nombre;
        this.valorActual = calcularValorActual(); // Inicializar el valor actual
    }

    public void agregarAbono(double abono) {
        abonosMensuales.add(abono);
        this.valorActual = calcularValorActual(); // Actualizar el valor actual cada vez que se agrega un abono
    }

    public double calcularMensualidad() {
        return montoMeta / ((Math.pow(1 + tasaRetorno, plazoMeses) - 1) / tasaRetorno);
    }

    public double calcularTotalDineroAnadido() {
        return abonosMensuales.stream().mapToDouble(Double::doubleValue).sum();
    }

    public double calcularDineroGanado() {
        double totalConInteres = 0;
        for (int i = 0; i < abonosMensuales.size(); i++) {
            totalConInteres += abonosMensuales.get(i) * Math.pow(1 + tasaRetorno, plazoMeses - i - 1);
        }
        return totalConInteres - calcularTotalDineroAnadido();
    }

    private double calcularValorActual() {
        double valorActual = 0;
        for (int i = 0; i < abonosMensuales.size(); i++) {
            valorActual += abonosMensuales.get(i) * Math.pow(1 + tasaRetorno, plazoMeses - i - 1);
        }
        return valorActual;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public double getMontoMeta() {
        return montoMeta;
    }

    public double getTasaRetorno() {
        return tasaRetorno;
    }

    public List<Double> getAbonosMensuales() {
        return abonosMensuales;
    }

    public double getValorActual() {
        return valorActual; // Nuevo método getter para el valor actual
    }

    public String getNombre() {
        return nombre;
    }

}

