package com.example.finanzaspro;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Movimiento {
    private Categoria categoria;
    private String titulo;
    private String tipo;
    private double cantidad;
    private LocalDate fecha;
    private boolean esRecurrente;
    private int intervaloDias;

    public Movimiento(Categoria categoria, String tipo ,String titulo, double cantidad, LocalDate fecha, boolean esRecurrente, int intervaloDias) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.esRecurrente = esRecurrente;
        this.intervaloDias = intervaloDias;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean isEsRecurrente() {
        return esRecurrente;
    }

    public int getIntervaloDias() {
        return intervaloDias;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCategoria(Categoria categoriaSeleccionada) {
        this.categoria = categoriaSeleccionada;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setEsRecurrente(boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    public void setIntervaloDias(int intervaloDias) {
        this.intervaloDias = intervaloDias;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public long calcularDiasRestantes() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaRegistro = this.getFecha();
        long diasDesdeRegistro = ChronoUnit.DAYS.between(fechaRegistro, fechaActual);
        long ciclosCompletos = diasDesdeRegistro / this.getIntervaloDias();

        LocalDate fechaUltimoMovimiento = fechaRegistro.plusDays(ciclosCompletos * this.getIntervaloDias());
        LocalDate fechaProximoMovimiento = fechaUltimoMovimiento.plusDays(this.getIntervaloDias());

        return ChronoUnit.DAYS.between(fechaActual, fechaProximoMovimiento);
    }
}

