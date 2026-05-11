package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "TurnoRequest", description = "Datos para crear un turno. El profesionalId se toma del path.")
public class TurnoRequest {

    @Schema(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del paciente es obligatorio")
    private UUID pacienteId;

    @Schema(description = "Fecha y hora futura del turno", example = "2026-05-10T15:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha y hora del turno es obligatoria")
    @Future(message = "La fecha del turno no puede ser en el pasado")
    private LocalDateTime fechaHora;

    @ArraySchema(schema = @Schema(description = "UUID de servicio incluido en el turno", example = "750e8400-e29b-41d4-a716-446655440000"), minItems = 1)
    @NotNull(message = "La lista de servicios es obligatoria")
    @NotEmpty(message = "El turno debe incluir al menos un servicio")
    private List<@NotNull(message = "El ID del servicio no puede ser nulo") UUID> servicioIds;

    @Schema(description = "Notas opcionales sobre el turno", example = "Primera sesión")
    private String observaciones;
}
