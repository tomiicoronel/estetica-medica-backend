package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "BloqueoAgendaResponse", description = "Datos devueltos por la API para un bloqueo de agenda con rango half-open [fechaInicio, fechaFin).")
public class BloqueoAgendaResponse {

    @Schema(description = "Identificador único UUID del bloqueo", example = "a50e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID de la profesional dueña del bloqueo", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID profesionalId;

    @Schema(description = "Fecha y hora de inicio del bloqueo, incluida en el rango", example = "2026-06-10T14:00:00")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha y hora de fin del bloqueo, excluida del rango", example = "2026-06-10T16:00:00")
    private LocalDateTime fechaFin;

    @Schema(description = "Motivo opcional del bloqueo", example = "Capacitación fuera del consultorio")
    private String motivo;

    @Schema(description = "Fecha y hora de creación", example = "2026-05-18T10:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-05-18T11:00:00")
    private LocalDateTime actualizadoEn;
}

