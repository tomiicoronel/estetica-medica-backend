package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "DashboardResponse", description = "Métricas del dashboard de la profesional autenticada calculadas para una fecha específica.")
public class DashboardResponse {

    @Schema(description = "Fecha para la que se calcularon las métricas", example = "2026-06-05")
    private LocalDate fecha;

    @Schema(description = "Cantidad de turnos agendados para la fecha, en cualquier estado", example = "8")
    private long cantidadTurnos;

    @Schema(description = "Cantidad de turnos en estado REALIZADO para la fecha", example = "5")
    private long cantidadTurnosRealizados;

    @Schema(description = "Cantidad total de pacientes activos de la profesional. No depende de la fecha.", example = "120")
    private long pacientesActivos;

    @Schema(description = "Total recaudado en la fecha, sumando los pagos cuya fecha cae en ese día", example = "85000.00")
    private BigDecimal totalRecaudado;
}
