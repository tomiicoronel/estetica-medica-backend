package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AuthResponse", description = "Respuesta devuelta cuando el login es correcto.")
public class AuthResponse {

    @Schema(description = "Token JWT que debe enviarse en Authorization: Bearer <token>")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    @Builder.Default
    private String tipoToken = "Bearer";
}

