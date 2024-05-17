package com.example.finanzaspro;

import java.time.LocalDate;

public class Movimiento {
    private Categoria categoria;
    private String titulo;
    private double cantidad;
    private LocalDate fecha;
    private boolean esRecurrente;
    private int intervaloDias;

    public Movimiento(Categoria categoria, String titulo, double cantidad, LocalDate fecha, boolean esRecurrente, int intervaloDias) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.esRecurrente = esRecurrente;
        this.intervaloDias = intervaloDias;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isEsRecurrente() {
        return esRecurrente;
    }

    public void setEsRecurrente(boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    public int getIntervaloDias() {
        return intervaloDias;
    }

    public void setIntervaloDias(int intervaloDias) {
        this.intervaloDias = intervaloDias;
    }
}

