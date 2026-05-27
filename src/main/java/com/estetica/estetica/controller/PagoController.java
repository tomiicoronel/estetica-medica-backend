package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.PagoRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.PagoResponse;
import com.estetica.estetica.dto.response.ResumenPagoResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.PagoService;
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
@Tag(name = "Pagos", description = "Operaciones para registrar pagos, consultar deuda y listar pagos por turno o profesional.")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/api/turnos/{turnoId}/pagos")
    @Operation(summary = "Registrar pago", description = "Registra un pago parcial o total para un turno. El pago combinado se logra creando varios pagos para el mismo turno.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, trueque sin detalle o pago superior al monto total",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PagoResponse> registrar(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID turnoId,
            @Valid @RequestBody PagoRequest request) {
        PagoResponse response = pagoService.registrar(turnoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/turnos/{turnoId}/pagos")
    @Operation(summary = "Listar pagos de un turno", description = "Devuelve todos los pagos registrados para un turno, ordenados por fecha ascendente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<PagoResponse>> listarPorTurno(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID turnoId) {
        return ResponseEntity.ok(pagoService.listarPorTurno(turnoId));
    }

    @GetMapping("/api/turnos/{turnoId}/pagos/resumen")
    @Operation(summary = "Obtener resumen de pagos", description = "Devuelve el monto total del turno, la suma pagada, la deuda pendiente y el detalle de pagos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ResumenPagoResponse> obtenerResumen(
            @Parameter(description = "UUID del turno", example = "850e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID turnoId) {
        return ResponseEntity.ok(pagoService.obtenerResumen(turnoId));
    }

    @GetMapping("/api/pagos")
    @Operation(summary = "Listar pagos", description = "Devuelve todos los pagos asociados a turnos de la profesional autenticada. Sirve como base para reportes futuros.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<PagoResponse>> listarPorProfesional() {
        return ResponseEntity.ok(pagoService.listarPorProfesional());
    }

    @DeleteMapping("/api/pagos/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un pago solo si el turno asociado todavía no está en estado REALIZADO, para conservar integridad histórica.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "El pago pertenece a un turno realizado y no puede eliminarse",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "UUID del pago", example = "930e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

