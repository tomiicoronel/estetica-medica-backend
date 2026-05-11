package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.HistoriaClinicaCorporalRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.HistoriaClinicaCorporalResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.HistoriaClinicaCorporalService;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Historia Clínica Corporal", description = "Operaciones para crear, consultar y actualizar fichas clínicas corporales.")
public class HistoriaClinicaCorporalController {

    private final HistoriaClinicaCorporalService service;

    @PostMapping("/api/pacientes/{pacienteId}/historia-clinica-corporal")
    @Operation(summary = "Crear historia clínica corporal", description = "Crea una ficha corporal para un paciente. Cada paciente puede tener una sola ficha corporal.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ficha corporal creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "El paciente ya tiene ficha corporal",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HistoriaClinicaCorporalResponse> crear(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId,
            @Valid @RequestBody HistoriaClinicaCorporalRequest request) {
        HistoriaClinicaCorporalResponse response = service.crearFicha(pacienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/pacientes/{pacienteId}/historia-clinica-corporal")
    @Operation(summary = "Obtener historia clínica corporal", description = "Devuelve la ficha corporal asociada a un paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ficha corporal encontrada"),
            @ApiResponse(responseCode = "404", description = "Paciente o ficha corporal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HistoriaClinicaCorporalResponse> obtenerPorPaciente(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId) {
        HistoriaClinicaCorporalResponse response = service.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/historia-clinica-corporal/{id}")
    @Operation(summary = "Actualizar historia clínica corporal", description = "Actualiza una ficha corporal existente. El frontend puede enviar el JSON completo con un solo campo modificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ficha corporal actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Ficha corporal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HistoriaClinicaCorporalResponse> actualizar(
            @Parameter(description = "UUID de la ficha corporal", example = "960e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody HistoriaClinicaCorporalRequest request) {
        HistoriaClinicaCorporalResponse response = service.actualizarFicha(id, request);
        return ResponseEntity.ok(response);
    }
}
