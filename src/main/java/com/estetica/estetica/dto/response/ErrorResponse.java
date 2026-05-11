package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ErrorResponse", description = "Respuesta estándar para errores generales de la API.")
public class ErrorResponse {

    @Schema(description = "Fecha y hora en la que ocurrió el error", example = "2026-05-07T18:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código HTTP devuelto por la API", example = "404")
    private Integer status;

    @Schema(description = "Descripción corta del tipo de error", example = "No encontrado")
    private String error;

    @Schema(description = "Mensaje legible con el detalle del error", example = "Profesional no encontrada con ID: 550e8400-e29b-41d4-a716-446655440000")
    private String mensaje;
}

