package com.estetica.estetica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ServicioRequest", description = "Datos para crear o actualizar un servicio de una profesional.")
public class ServicioRequest {


    @Schema(description = "Nombre del servicio", example = "Limpieza facial profunda", maxLength = 150, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    @Schema(description = "Descripción detallada del servicio", example = "Tratamiento de limpieza facial con extracción de comedones", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La descripción del servicio es obligatoria")
    private String descripcion;

    @Schema(description = "Precio actual del servicio", example = "15000.00", minimum = "0.01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
}
