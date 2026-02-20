package com.universidad.ligabasket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDTO {

    @NotNull(message = "Los puntos del equipo local son obligatorios")
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer equipoLocalPuntos;

    @NotNull(message = "Los puntos del equipo visitante son obligatorios")
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer equipoVisitantePuntos;

    private String observaciones;
}
