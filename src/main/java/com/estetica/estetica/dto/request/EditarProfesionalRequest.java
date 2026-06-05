package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Schema(name = "EditarProfesionalRequest", description = "Datos editables de una profesional desde administración.")
public class EditarProfesionalRequest {

    @Schema(description = "Nombre de la profesional", example = "María", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Schema(description = "Apellido de la profesional", example = "García", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    @Schema(description = "Email único de la profesional", example = "maria@email.com", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "1155443322", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Pattern(regexp = "^[0-9+()\\-\\s]+$", message = "El teléfono solo puede contener números, espacios y los signos + - ( )")
    private String telefono;

    @Schema(description = "Especialidad de la profesional", example = "Dermatología estética", maxLength = 100)
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;
}
