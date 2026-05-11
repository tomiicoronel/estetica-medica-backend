package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PacienteResponse", description = "Datos devueltos por la API para un paciente.")
public class PacienteResponse {

    @Schema(description = "Identificador único UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "UUID de la profesional que atiende al paciente", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID profesionalId;

    @Schema(description = "Nombre del paciente", example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del paciente", example = "Pérez")
    private String apellido;

    @Schema(description = "DNI o CUIT", example = "30111222")
    private String dniCuit;

    @Schema(description = "Fecha de nacimiento", example = "1990-05-20")
    private LocalDate fechaNacimiento;

    @Schema(description = "Teléfono de contacto", example = "1122334455")
    private String telefono;

    @Schema(description = "Email del paciente", example = "ana@email.com")
    private String email;

    @Schema(description = "Profesión u ocupación", example = "Docente")
    private String profesion;

    @Schema(description = "Domicilio", example = "Av. Siempre Viva 742")
    private String domicilio;

    @Schema(description = "Obra social", example = "OSDE")
    private String obraSocial;

    @Schema(description = "Número de afiliado de obra social", example = "123456789")
    private String numeroObraSocial;

    @Schema(description = "Nombre del contacto de emergencia", example = "Laura Pérez")
    private String contactoEmergenciaNombre;

    @Schema(description = "Teléfono del contacto de emergencia", example = "1199887766")
    private String contactoEmergenciaTelefono;

    @Schema(description = "Parentesco del contacto de emergencia", example = "Hermana")
    private String contactoEmergenciaParentesco;

    @Schema(description = "Primera entidad de traslado", example = "Hospital Italiano")
    private String entidadTraslado1;

    @Schema(description = "Segunda entidad de traslado", example = "Sanatorio Güemes")
    private String entidadTraslado2;

    @Schema(description = "Indica si el paciente está activo o archivado", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha y hora de creación", example = "2026-04-24T10:30:00")
    private LocalDateTime creadoEn;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-04-24T10:30:00")
    private LocalDateTime actualizadoEn;
}
