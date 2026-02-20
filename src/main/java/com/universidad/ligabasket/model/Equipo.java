package com.universidad.ligabasket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "equipos")
public class Equipo {

    @Id
    private String id;
    private String nombre;
    private String universidad;
    private String ciudad;
    private String logoUrl;
    private String colorPrimario;
    private String colorSecundario;
    private LocalDate fechaFundacion;
    private Integer victorias;
    private Integer derrotas;
    private Integer puntos;

    public Equipo() {
    }

    public Equipo(String id, String nombre, String universidad, String ciudad, String logoUrl, String colorPrimario,
            String colorSecundario, LocalDate fechaFundacion, Integer victorias, Integer derrotas,
            Integer puntos) {
        this.id = id;
        this.nombre = nombre;
        this.universidad = universidad;
        this.ciudad = ciudad;
        this.logoUrl = logoUrl;
        this.colorPrimario = colorPrimario;
        this.colorSecundario = colorSecundario;
        this.fechaFundacion = fechaFundacion;
        this.victorias = victorias;
        this.derrotas = derrotas;
        this.puntos = puntos;
    }

    public Equipo(String nombre, String universidad, String ciudad, String colorPrimario, String colorSecundario) {
        this.nombre = nombre;
        this.universidad = universidad;
        this.ciudad = ciudad;
        this.colorPrimario = colorPrimario;
        this.colorSecundario = colorSecundario;
        this.fechaFundacion = LocalDate.now();
        this.victorias = 0;
        this.derrotas = 0;
        this.puntos = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getColorPrimario() {
        return colorPrimario;
    }

    public void setColorPrimario(String colorPrimario) {
        this.colorPrimario = colorPrimario;
    }

    public String getColorSecundario() {
        return colorSecundario;
    }

    public void setColorSecundario(String colorSecundario) {
        this.colorSecundario = colorSecundario;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public Integer getVictorias() {
        return victorias;
    }

    public void setVictorias(Integer victorias) {
        this.victorias = victorias;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }
}