package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.CrearProfesionalRequest;
import com.estetica.estetica.dto.request.EditarProfesionalRequest;
import com.estetica.estetica.dto.request.ResetearPasswordRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Administración", description = "Operaciones administrativas para gestionar profesionales.")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/profesionales")
    @Operation(summary = "Crear profesional", description = "Crea una nueva cuenta profesional. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profesional creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfesionalResponse> crearProfesional(@Valid @RequestBody CrearProfesionalRequest request) {
        ProfesionalResponse response = adminService.crearProfesional(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profesionales")
    @Operation(summary = "Listar profesionales", description = "Devuelve las profesionales registradas. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<ProfesionalResponse>> listarProfesionales() {
        return ResponseEntity.ok(adminService.listarProfesionales());
    }

    @PutMapping("/profesionales/{id}")
    @Operation(summary = "Editar profesional", description = "Actualiza los datos básicos de una profesional. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfesionalResponse> editarProfesional(
            @PathVariable UUID id,
            @Valid @RequestBody EditarProfesionalRequest request
    ) {
        return ResponseEntity.ok(adminService.editarProfesional(id, request));
    }

    @PostMapping("/profesionales/{id}/resetear-password")
    @Operation(summary = "Resetear contraseña", description = "Asigna una nueva contraseña inicial y obliga a cambiarla en el próximo ingreso. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contraseña reseteada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> resetearPassword(
            @PathVariable UUID id,
            @Valid @RequestBody ResetearPasswordRequest request
    ) {
        adminService.resetearPassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/profesionales/{id}")
    @Operation(summary = "Dar de baja profesional", description = "Elimina una profesional existente. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profesional dada de baja correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profesional no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> darDeBaja(@PathVariable UUID id) {
        adminService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}
