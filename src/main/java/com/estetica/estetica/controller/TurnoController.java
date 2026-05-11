package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.TurnoRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.TurnoResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.model.EstadoTurno;
import com.estetica.estetica.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Turnos", description = "Operaciones para crear, consultar y cambiar el estado de turnos.")
public class TurnoController {

    private final TurnoService turnoService;

    @PostMapping("/api/profesionales/{profesionalId}/turnos")
    @Operation(summary = "Crear turno", description = "Crea un turno para una profesional, paciente y uno o más servicios. Congela el precio de cada servicio en el momento de creación.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Turno creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, fecha pasada, paciente de otra profesional o servicio inactivo",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Profesional, paciente o servicio no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TurnoResponse> crear(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID profesionalId,
            @Valid @RequestBody TurnoRequest request) {
        TurnoResponse response = turnoService.crear(profesionalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/profesionales/{profesionalId}/turnos")
    @Operation(summary = "Listar turnos de una profesional", description = "Lista turnos por profesional. Permite filtrar por estado o por rango de fechas usando parámetros opcionales.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TurnoResponse>> listarPorProfesional(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID profesionalId,
            @Parameter(description = "Estado opcional para filtrar", example = "PENDIENTE", schema = @Schema(implementation = EstadoTurno.class))
            @RequestParam(required = false) EstadoTurno estado,
            @Parameter(description = "Inicio del rango de fechas en formato ISO-8601", example = "2026-04-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fin del rango de fechas en formato ISO-8601", example = "2026-04-30T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        if (estado != null) {
            return ResponseEntity.ok(turnoService.listarPorProfesionalYEstado(profesionalId, estado));
        }
        if (desde != null || hasta != null) {
            return ResponseEntity.ok(turnoService.listarPorProfesionalYRango(profesionalId, desde, hasta));
        }
        return ResponseEntity.ok(turnoService.listarPorProfesional(profesionalId));
    }

    @GetMapping("/api/pacientes/{pacienteId}/turnos")
    @Operation(summary = "Listar turnos de un paciente", description = "Devuelve todos los turnos asociados a un paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<TurnoResponse>> listarPorPaciente(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId) {
        return ResponseEntity.ok(turnoService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/api/turnos/{id}")
    @Operation(summary = "Buscar turno por ID", description = "Devuelve un turno por UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TurnoResponse> buscarPorId(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        return ResponseEntity.ok(turnoService.buscarPorId(id));
    }

    @PatchMapping("/api/turnos/{id}/estado")
    @Operation(summary = "Cambiar estado de turno", description = "Cambia el estado respetando las transiciones permitidas: PENDIENTE→CONFIRMADO/CANCELADO, CONFIRMADO→REALIZADO/CANCELADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Estado o transición inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TurnoResponse> cambiarEstado(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevo estado del turno", example = "CONFIRMADO", schema = @Schema(implementation = EstadoTurno.class))
            @RequestParam EstadoTurno nuevoEstado) {
        return ResponseEntity.ok(turnoService.cambiarEstado(id, nuevoEstado));
    }
}
