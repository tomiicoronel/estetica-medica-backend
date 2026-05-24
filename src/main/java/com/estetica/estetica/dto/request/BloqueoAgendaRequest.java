package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "BloqueoAgendaRequest", description = "Datos para crear o actualizar un bloqueo de agenda con rango half-open [fechaInicio, fechaFin). El profesionalId se toma del path al crear.")
public class BloqueoAgendaRequest {

    @Schema(description = "Fecha y hora de inicio del bloqueo, incluida en el rango", example = "2026-06-10T14:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha de inicio del bloqueo es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio del bloqueo no puede estar en el pasado")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha y hora de fin del bloqueo, excluida del rango. Debe ser posterior a fechaInicio", example = "2026-06-10T16:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha de fin del bloqueo es obligatoria")
    private LocalDateTime fechaFin;

    @Schema(description = "Motivo opcional del bloqueo", example = "Capacitación fuera del consultorio", maxLength = 200)
    @Size(max = 200, message = "El motivo no puede superar los 200 caracteres")
    private String motivo;
}

