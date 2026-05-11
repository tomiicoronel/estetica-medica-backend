package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.HistoriaClinicaFacialRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.HistoriaClinicaFacialResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.HistoriaClinicaFacialService;
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
@Tag(name = "Historia Clínica Facial", description = "Operaciones para crear, consultar y actualizar fichas clínicas faciales.")
public class HistoriaClinicaFacialController {

    private final HistoriaClinicaFacialService service;

    @PostMapping("/api/pacientes/{pacienteId}/historia-clinica-facial")
    @Operation(summary = "Crear historia clínica facial", description = "Crea una ficha facial para un paciente. Cada paciente puede tener una sola ficha facial.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ficha facial creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "El paciente ya tiene ficha facial",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HistoriaClinicaFacialResponse> crear(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId,
            @Valid @RequestBody HistoriaClinicaFacialRequest request) {
        HistoriaClinicaFacialResponse response = service.crearFicha(pacienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/pacientes/{pacienteId}/historia-clinica-facial")
    @Operation(summary = "Obtener historia clínica facial", description = "Devuelve la ficha facial asociada a un paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ficha facial encontrada"),
            @ApiResponse(responseCode = "404", description = "Paciente o ficha facial no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HistoriaClinicaFacialResponse> obtenerPorPaciente(
            @Parameter(description = "UUID del paciente", example = "650e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID pacienteId) {
        HistoriaClinicaFacialResponse response = service.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/historia-clinica-facial/{id}")
    @Operation(summary = "Actualizar historia clínica facial", description = "Actualiza una ficha facial existente. El frontend puede enviar el JSON completo con un solo campo modificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ficha facial actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Ficha facial no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HistoriaClinicaFacialResponse> actualizar(
            @Parameter(description = "UUID de la ficha facial", example = "950e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody HistoriaClinicaFacialRequest request) {
        HistoriaClinicaFacialResponse response = service.actualizarFicha(id, request);
        return ResponseEntity.ok(response);
    }
}
