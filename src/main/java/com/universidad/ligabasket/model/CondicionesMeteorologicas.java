package com.universidad.ligabasket.model;

public class CondicionesMeteorologicas {

    private Integer temperatura;
    private String descripcion;
    private Integer humedad;
    private String icono;

    public CondicionesMeteorologicas() {
    }

    public CondicionesMeteorologicas(Integer temperatura, String descripcion, Integer humedad, String icono) {
        this.temperatura = temperatura;
        this.descripcion = descripcion;
        this.humedad = humedad;
        this.icono = icono;
    }

    public Integer getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Integer temperatura) {
        this.temperatura = temperatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getHumedad() {
        return humedad;
    }

    public void setHumedad(Integer humedad) {
        this.humedad = humedad;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}