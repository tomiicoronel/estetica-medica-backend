package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.BloqueoAgendaRequest;
import com.estetica.estetica.dto.response.BloqueoAgendaResponse;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.BloqueoAgendaService;
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
@Tag(name = "Bloqueos de agenda", description = "Operaciones para bloquear rangos half-open [fechaInicio, fechaFin) en los que una profesional no atiende turnos.")
public class BloqueoAgendaController {

    private final BloqueoAgendaService bloqueoAgendaService;

    @PostMapping("/api/bloqueos-agenda")
    @Operation(summary = "Crear bloqueo de agenda", description = "Crea un bloqueo horario half-open [fechaInicio, fechaFin) para la profesional autenticada. Rechaza rangos solapados o rangos con turnos ya agendados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bloqueo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, rango en pasado, solapamiento o turnos existentes en el rango",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BloqueoAgendaResponse> crear(
            @Valid @RequestBody BloqueoAgendaRequest request) {
        BloqueoAgendaResponse response = bloqueoAgendaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/bloqueos-agenda")
    @Operation(summary = "Listar bloqueos de agenda", description = "Devuelve todos los bloqueos de agenda de la profesional autenticada ordenados por fecha de inicio ascendente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<BloqueoAgendaResponse>> listarPorProfesional() {
        return ResponseEntity.ok(bloqueoAgendaService.listarPorProfesional());
    }

    @GetMapping("/api/bloqueos-agenda/{id}")
    @Operation(summary = "Buscar bloqueo por ID", description = "Devuelve un bloqueo de agenda por su UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueo encontrado"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BloqueoAgendaResponse> buscarPorId(
            @Parameter(description = "UUID del bloqueo", example = "a50e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        return ResponseEntity.ok(bloqueoAgendaService.buscarPorId(id));
    }

    @PutMapping("/api/bloqueos-agenda/{id}")
    @Operation(summary = "Actualizar bloqueo de agenda", description = "Actualiza el rango y motivo de un bloqueo. Mantiene la profesional original y aplica las mismas validaciones que al crear.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bloqueo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, rango en pasado, solapamiento o turnos existentes en el rango",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BloqueoAgendaResponse> actualizar(
            @Parameter(description = "UUID del bloqueo", example = "a50e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody BloqueoAgendaRequest request) {
        return ResponseEntity.ok(bloqueoAgendaService.actualizar(id, request));
    }

    @DeleteMapping("/api/bloqueos-agenda/{id}")
    @Operation(summary = "Eliminar bloqueo de agenda", description = "Elimina un bloqueo de agenda por su UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bloqueo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Bloqueo no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "UUID del bloqueo", example = "a50e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        bloqueoAgendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

