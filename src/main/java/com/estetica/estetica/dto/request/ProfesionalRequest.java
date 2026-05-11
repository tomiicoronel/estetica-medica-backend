package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ProfesionalRequest", description = "Datos necesarios para crear o actualizar una profesional.")
public class ProfesionalRequest {

    @Schema(description = "Nombre de la profesional", example = "María", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Schema(description = "Apellido de la profesional", example = "García", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    @Schema(description = "Email único de la profesional. En el futuro funcionará como login.", example = "maria@email.com", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "1155443322", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;

    @Schema(description = "Especialidad de la profesional", example = "Dermatología estética", maxLength = 100)
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;
}
