package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.ProfesionalService;
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
@RequestMapping("/api/profesionales")
@RequiredArgsConstructor
@Tag(name = "Profesionales", description = "Operaciones para crear, consultar, actualizar y eliminar profesionales.")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @PostMapping
    @Operation(summary = "Crear profesional", description = "Registra una nueva profesional. El email debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesional creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class})))
    })
    public ResponseEntity<ProfesionalResponse> crear(@Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profesional por ID", description = "Devuelve los datos de una profesional a partir de su UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional encontrada"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfesionalResponse> buscarPorId(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        ProfesionalResponse response = profesionalService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar profesionales", description = "Devuelve todas las profesionales registradas.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<List<ProfesionalResponse>> listarTodos() {
        List<ProfesionalResponse> response = profesionalService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar profesional", description = "Actualiza los datos de una profesional existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfesionalResponse> actualizar(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id,
            @Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar profesional", description = "Elimina una profesional por UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profesional eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "UUID de la profesional", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
