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
@Schema(name = "ProfesionalResponse", description = "Datos devueltos por la API para una profesional.")
public class ProfesionalResponse {

    @Schema(description = "Identificador único UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Nombre de la profesional", example = "María")
    private String nombre;

    @Schema(description = "Apellido de la profesional", example = "García")
    private String apellido;

    @Schema(description = "Email de la profesional", example = "maria@email.com")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "1155443322")
    private String telefono;

    @Schema(description = "Especialidad de la profesional", example = "Dermatología estética")
    private String especialidad;

    @Schema(description = "Fecha y hora de creación", example = "2026-04-24T10:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-04-24T10:30:00")
    private LocalDateTime actualizadoEn;
}
