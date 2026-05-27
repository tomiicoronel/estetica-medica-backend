package com.estetica.estetica.controller;

import com.estetica.estetica.dto.request.ProfesionalRequest;
import com.estetica.estetica.dto.response.ErrorResponse;
import com.estetica.estetica.dto.response.ProfesionalResponse;
import com.estetica.estetica.dto.response.ValidationErrorResponse;
import com.estetica.estetica.service.ProfesionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profesionales")
@RequiredArgsConstructor
@Tag(name = "Profesionales", description = "Operaciones para consultar y actualizar el perfil de la profesional autenticada. La creación inicial se realiza por seeder.")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @GetMapping("/me")
    @Operation(summary = "Obtener mi perfil", description = "Devuelve los datos de la profesional autenticada según el token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional encontrada"),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfesionalResponse> obtenerMiPerfil() {
        ProfesionalResponse response = profesionalService.obtenerPerfilAutenticado();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @Operation(summary = "Actualizar mi perfil", description = "Actualiza los datos de la profesional autenticada. No permite modificar perfiles de otras profesionales.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profesional actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email duplicado",
                    content = @Content(schema = @Schema(oneOf = {ErrorResponse.class, ValidationErrorResponse.class}))),
            @ApiResponse(responseCode = "404", description = "Profesional autenticada no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfesionalResponse> actualizarMiPerfil(@Valid @RequestBody ProfesionalRequest request) {
        ProfesionalResponse response = profesionalService.actualizarPerfilAutenticado(request);
        return ResponseEntity.ok(response);
    }
}
