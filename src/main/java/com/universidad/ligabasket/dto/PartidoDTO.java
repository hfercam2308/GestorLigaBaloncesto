package com.universidad.ligabasket.dto;

import com.universidad.ligabasket.model.CondicionesMeteorologicas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartidoDTO {

    private String id;
    private String equipoLocalNombre;
    private String equipoVisitanteNombre;
    private String pabellonNombre;
    private LocalDate fecha;
    private String hora;
    private Integer jornada;
    private String estado;
    private String observaciones;
    private CondicionesMeteorologicas condicionesMeteorologicas;

    // Campos de resultado
    private Integer equipoLocalPuntos;
    private Integer equipoVisitantePuntos;
    private String equipoGanadorId;
}
