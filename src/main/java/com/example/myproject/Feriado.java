package com.example.myproject;

import java.time.LocalDate;

public class Feriado {
    private String motivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean esNacional;
    private boolean esRecuperable;

    public Feriado() {
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isEsNacional() {
        return esNacional;
    }

    public void setEsNacional(boolean esNacional) {
        this.esNacional = esNacional;
    }

    public boolean isEsRecuperable() {
        return esRecuperable;
    }

    public void setEsRecuperable(boolean esRecuperable) {
        this.esRecuperable = esRecuperable;
    }
}
