package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CambioPasswordRequest", description = "Datos necesarios para cambiar la contraseña de una profesional autenticada.")
public class CambioPasswordRequest {

    @Schema(description = "Contraseña actual de la profesional", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;

    @Schema(description = "Nueva contraseña de la profesional", example = "NuevaPassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    private String passwordNueva;
}
