package com.universidad.ligabasket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

// Clase que representa un Partido en la base de datos (Colección MongoDB)
@Document(collection = "partidos")
public class Partido {

    @Id
    private String id;
    private String equipoLocalId;
    private String equipoVisitanteId;

    // Objeto incrustado con la información del lugar del encuentro
    private DatosPabellon datosPabellon;

    private LocalDate fecha;
    private String hora;
    private Integer jornada;
    private String estado;

    // Observaciones adicionales del partido
    private String observaciones;

    // Almacena el clima consultado de la API externa para este partido
    private CondicionesMeteorologicas condicionesMeteorologicas;

    // Marcadores finales (se rellenan al finalizar el partido)
    private Integer equipoLocalPuntos;
    private Integer equipoVisitantePuntos;
    private String equipoGanadorId;

    public Partido() {
    }

    public Partido(String id, String equipoLocalId, String equipoVisitanteId, DatosPabellon datosPabellon,
            LocalDate fecha, String hora, Integer jornada, String estado, String observaciones,
            CondicionesMeteorologicas condicionesMeteorologicas, Integer equipoLocalPuntos,
            Integer equipoVisitantePuntos, String equipoGanadorId) {
        this.id = id;
        this.equipoLocalId = equipoLocalId;
        this.equipoVisitanteId = equipoVisitanteId;
        this.datosPabellon = datosPabellon;
        this.fecha = fecha;
        this.hora = hora;
        this.jornada = jornada;
        this.estado = estado;
        this.observaciones = observaciones;
        this.condicionesMeteorologicas = condicionesMeteorologicas;
        this.equipoLocalPuntos = equipoLocalPuntos;
        this.equipoVisitantePuntos = equipoVisitantePuntos;
        this.equipoGanadorId = equipoGanadorId;
    }

    // Constructor para programación manual de partidos
    public Partido(String equipoLocalId, String equipoVisitanteId, DatosPabellon datosPabellon,
            LocalDate fecha, String hora, Integer jornada) {
        this.equipoLocalId = equipoLocalId;
        this.equipoVisitanteId = equipoVisitanteId;
        this.datosPabellon = datosPabellon;
        this.fecha = fecha;
        this.hora = hora;
        this.jornada = jornada;
        this.estado = "PROGRAMADO";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipoLocalId() {
        return equipoLocalId;
    }

    public void setEquipoLocalId(String equipoLocalId) {
        this.equipoLocalId = equipoLocalId;
    }

    public String getEquipoVisitanteId() {
        return equipoVisitanteId;
    }

    public void setEquipoVisitanteId(String equipoVisitanteId) {
        this.equipoVisitanteId = equipoVisitanteId;
    }

    public DatosPabellon getDatosPabellon() {
        return datosPabellon;
    }

    public void setDatosPabellon(DatosPabellon datosPabellon) {
        this.datosPabellon = datosPabellon;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getJornada() {
        return jornada;
    }

    public void setJornada(Integer jornada) {
        this.jornada = jornada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public CondicionesMeteorologicas getCondicionesMeteorologicas() {
        return condicionesMeteorologicas;
    }

    public void setCondicionesMeteorologicas(CondicionesMeteorologicas condicionesMeteorologicas) {
        this.condicionesMeteorologicas = condicionesMeteorologicas;
    }

    public Integer getEquipoLocalPuntos() {
        return equipoLocalPuntos;
    }

    public void setEquipoLocalPuntos(Integer equipoLocalPuntos) {
        this.equipoLocalPuntos = equipoLocalPuntos;
    }

    public Integer getEquipoVisitantePuntos() {
        return equipoVisitantePuntos;
    }

    public void setEquipoVisitantePuntos(Integer equipoVisitantePuntos) {
        this.equipoVisitantePuntos = equipoVisitantePuntos;
    }

    public String getEquipoGanadorId() {
        return equipoGanadorId;
    }

    public void setEquipoGanadorId(String equipoGanadorId) {
        this.equipoGanadorId = equipoGanadorId;
    }
}
