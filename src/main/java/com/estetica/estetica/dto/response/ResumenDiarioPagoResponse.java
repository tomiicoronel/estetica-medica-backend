package com.estetica.estetica.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ResumenDiarioPagoResponse", description = "Total recaudado por la profesional autenticada en una fecha y el detalle de los pagos de ese día.")
public class ResumenDiarioPagoResponse {

    @Schema(description = "Fecha consultada", example = "2026-06-05")
    private LocalDate fecha;

    @Schema(description = "Suma de todos los pagos cuya fecha cae en el día consultado", example = "85000.00")
    private BigDecimal totalRecaudado;

    @Schema(description = "Cantidad de pagos registrados en el día consultado", example = "6")
    private int cantidadPagos;

    @ArraySchema(schema = @Schema(implementation = PagoResponse.class))
    private List<PagoResponse> pagos;
}
