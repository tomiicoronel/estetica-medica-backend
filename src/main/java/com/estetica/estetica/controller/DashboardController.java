package com.estetica.estetica.controller;

import com.estetica.estetica.dto.response.DashboardResponse;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Métricas del día para la profesional autenticada.")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    @Operation(summary = "Resumen del dashboard por día", description = "Devuelve las métricas de la profesional autenticada para una fecha (por defecto hoy): cantidad de turnos del día, turnos realizados del día, total de pacientes activos y total recaudado del día. Respeta el aislamiento por profesional.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Fecha con formato inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DashboardResponse> obtenerResumen(
            @Parameter(description = "Fecha a consultar en formato ISO-8601 (yyyy-MM-dd). Si se omite, se usa la fecha de hoy.", example = "2026-06-05")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(dashboardService.obtenerResumen(fecha));
    }
}
