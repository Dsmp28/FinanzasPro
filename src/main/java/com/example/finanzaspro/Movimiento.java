package com.example.finanzaspro;

import java.time.LocalDate;

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

}

