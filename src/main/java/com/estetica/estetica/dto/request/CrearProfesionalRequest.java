package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(name = "CrearProfesionalRequest", description = "Datos necesarios para crear una profesional desde administración.")
public class CrearProfesionalRequest {

    @Schema(description = "Nombre de la profesional", example = "María", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido de la profesional", example = "García", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Email único de la profesional", example = "maria@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "1155443322", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Schema(description = "Especialidad de la profesional", example = "Dermatología estética")
    private String especialidad;

    @Schema(description = "Contraseña inicial de la profesional", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}
