package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PacienteRequest", description = "Datos para crear o actualizar un paciente. Nombre, apellido, DNI/CUIT y teléfono son obligatorios.")
public class PacienteRequest {

    @Schema(description = "Campo legacy ignorado por la API. La profesional dueña se obtiene desde el token JWT.", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID profesionalId;

    @Schema(description = "Nombre del paciente", example = "Ana", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Schema(description = "Apellido del paciente", example = "Pérez", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    @Schema(description = "DNI o CUIT. No puede repetirse dentro de la misma profesional.", example = "30111222", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El DNI/CUIT es obligatorio")
    @Size(max = 20, message = "El DNI/CUIT no puede superar los 20 caracteres")
    @Pattern(regexp = "^[0-9.\\-]+$", message = "El DNI/CUIT solo puede contener números, puntos o guiones")
    private String dniCuit;

    @Schema(description = "Fecha de nacimiento", example = "1990-05-20")
    private LocalDate fechaNacimiento;

    @Schema(description = "Teléfono de contacto", example = "1122334455", maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Pattern(regexp = "^[0-9+()\\-\\s]+$", message = "El teléfono solo puede contener números, espacios y los signos + - ( )")
    private String telefono;

    @Schema(description = "Email del paciente. Opcional, pero si se envía debe ser válido.", example = "ana@email.com", maxLength = 150)
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Schema(description = "Profesión u ocupación", example = "Docente", maxLength = 100)
    @Size(max = 100, message = "La profesión no puede superar los 100 caracteres")
    private String profesion;

    @Schema(description = "Domicilio", example = "Av. Siempre Viva 742", maxLength = 255)
    @Size(max = 255, message = "El domicilio no puede superar los 255 caracteres")
    private String domicilio;

    @Schema(description = "Obra social", example = "OSDE", maxLength = 100)
    @Size(max = 100, message = "La obra social no puede superar los 100 caracteres")
    private String obraSocial;

    @Schema(description = "Número de afiliado de obra social", example = "123456789", maxLength = 50)
    @Size(max = 50, message = "El número de obra social no puede superar los 50 caracteres")
    private String numeroObraSocial;

    @Schema(description = "Nombre del contacto de emergencia", example = "Laura Pérez", maxLength = 100)
    @Size(max = 100, message = "El nombre del contacto de emergencia no puede superar los 100 caracteres")
    private String contactoEmergenciaNombre;

    @Schema(description = "Teléfono del contacto de emergencia", example = "1199887766", maxLength = 20)
    @Size(max = 20, message = "El teléfono del contacto de emergencia no puede superar los 20 caracteres")
    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "El teléfono del contacto de emergencia solo puede contener números, espacios y los signos + - ( )")
    private String contactoEmergenciaTelefono;

    @Schema(description = "Parentesco del contacto de emergencia", example = "Hermana", maxLength = 50)
    @Size(max = 50, message = "El parentesco no puede superar los 50 caracteres")
    private String contactoEmergenciaParentesco;

    @Schema(description = "Primera entidad de traslado", example = "Hospital Italiano", maxLength = 100)
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    private String entidadTraslado1;

    @Schema(description = "Segunda entidad de traslado", example = "Sanatorio Güemes", maxLength = 100)
    @Size(max = 100, message = "La entidad de traslado no puede superar los 100 caracteres")
    private String entidadTraslado2;
}
