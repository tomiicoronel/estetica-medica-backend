package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.PacienteRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.PacienteResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Operaciones para gestionar pacientes de una profesional.")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/api/profesionales/{profesionalId}/pacientes")
    @Operation(summary = "Crear paciente", description = "Crea un paciente asociado a una profesional. El DNI/CUIT no puede repetirse dentro de la misma profesional.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o DNI/CUIT duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PacienteResponse> crear(
            @Parameter(description = "UUID de la profesional dueña del paciente", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID profesionalId,
            @Valid @RequestBody PacienteRequest request) {
        request.setProfesionalId(profesionalId);
        PacienteResponse response = pacienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/profesionales/{profesionalId}/pacientes")
    @Operation(summary = "Listar pacientes por profesional", description = "Devuelve todos los pacientes de una profesional, activos y archivados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<PacienteResponse>> listarPorProfesional(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID profesionalId) {
        List<PacienteResponse> response = pacienteService.listarPorProfesional(profesionalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/profesionales/{profesionalId}/pacientes/activos")
    @Operation(summary = "Listar pacientes activos", description = "Devuelve solo los pacientes activos de una profesional.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<PacienteResponse>> listarActivosPorProfesional(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID profesionalId) {
        List<PacienteResponse> response = pacienteService.listarActivosPorProfesional(profesionalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/pacientes/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Devuelve un paciente por UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PacienteResponse> buscarPorId(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        PacienteResponse response = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/pacientes/{id}")
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o DNI/CUIT duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PacienteResponse> actualizar(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/pacientes/{id}/estado")
    @Operation(summary = "Activar o archivar paciente", description = "Cambia el estado activo del paciente sin borrar su historial clínico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> cambiarEstado(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "true para activar, false para archivar", example = "false")
            @RequestParam Boolean activo) {
        pacienteService.cambiarEstado(id, activo);
        String estado = activo ? "activado" : "archivado";
        return ResponseEntity.ok("Paciente " + estado + " correctamente");
    }
}
