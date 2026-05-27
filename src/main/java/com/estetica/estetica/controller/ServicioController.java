package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.ServicioRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.ServicioResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.ServicioService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Operaciones para administrar los servicios ofrecidos por cada profesional.")
public class ServicioController {

    private final ServicioService servicioService;

    @PostMapping("/api/servicios")
    @Operation(summary = "Crear servicio", description = "Crea un servicio vinculado a la profesional autenticada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Servicio creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o servicio duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ServicioResponse> crear(
            @Valid @RequestBody ServicioRequest request) {
        ServicioResponse response = servicioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/servicios")
    @Operation(summary = "Listar servicios", description = "Devuelve todos los servicios de la profesional autenticada, activos e inactivos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ServicioResponse>> listarPorProfesional() {
        List<ServicioResponse> response = servicioService.listarPorProfesional();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/servicios/activos")
    @Operation(summary = "Listar servicios activos", description = "Devuelve solo los servicios activos y disponibles para agendar de la profesional autenticada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ServicioResponse>> listarActivosPorProfesional() {
        List<ServicioResponse> response = servicioService.listarActivosPorProfesional();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/servicios/{id}")
    @Operation(summary = "Buscar servicio por ID", description = "Devuelve un servicio por UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio encontrado"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ServicioResponse> buscarPorId(
            @Parameter(description = "UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        ServicioResponse response = servicioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/servicios/{id}")
    @Operation(summary = "Actualizar servicio", description = "Actualiza nombre, descripción y precio. El servicio conserva su profesional original.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicio actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o intento de reasignación",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ServicioResponse> actualizar(
            @Parameter(description = "UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody ServicioRequest request) {
        ServicioResponse response = servicioService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/servicios/{id}/estado")
    @Operation(summary = "Activar o desactivar servicio", description = "Cambia el estado activo del servicio sin eliminarlo de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> cambiarEstado(
            @Parameter(description = "UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "true para activar, false para desactivar", example = "false")
            @RequestParam Boolean activo) {
        servicioService.cambiarEstado(id, activo);
        String estado = activo ? "activado" : "desactivado";
        return ResponseEntity.ok("Servicio " + estado + " correctamente");
    }

    @PatchMapping("/api/servicios/{id}/precio")
    @Operation(summary = "Actualizar precio", description = "Actualiza únicamente el precio actual del servicio. Los turnos ya creados conservan su precio congelado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Precio actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Precio inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> actualizarPrecio(
            @Parameter(description = "UUID del servicio", example = "750e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Parameter(description = "Nuevo precio del servicio", example = "18000.00")
            @RequestParam BigDecimal nuevoPrecio) {
        servicioService.actualizarPrecio(id, nuevoPrecio);
        return ResponseEntity.ok("Precio actualizado a " + nuevoPrecio + " correctamente");
    }
}
