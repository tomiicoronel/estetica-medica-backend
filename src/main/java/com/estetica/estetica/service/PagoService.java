package com.estetica.estetica.service;

import com.estetica.estetica.dto.request.PagoRequest;
import com.estetica.estetica.dto.response.PagoResponse;
import com.estetica.estetica.dto.response.ResumenDiarioPagoResponse;
import com.estetica.estetica.dto.response.ResumenPagoResponse;
import com.estetica.estetica.exception.AccesoNoAutorizadoException;
import com.estetica.estetica.model.EstadoTurno;
import com.estetica.estetica.model.MetodoPago;
import com.estetica.estetica.model.Pago;
import com.estetica.estetica.model.Turno;
import com.estetica.estetica.repository.PagoRepository;
import com.estetica.estetica.repository.TurnoRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final TurnoRepository turnoRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional
    public PagoResponse registrar(UUID turnoId, PagoRequest request) {
        Turno turno = buscarTurnoPropio(turnoId);

        validarMetodo(request.getMetodo());
        validarMontoMayorACero(request.getMonto());
        validarTrueque(request);
        validarNoSuperaMontoTotal(turno, request.getMonto());

        boolean esTrueque = MetodoPago.TRUEQUE.equals(request.getMetodo());

        Pago pago = Pago.builder()
                .turno(turno)
                .metodo(request.getMetodo())
                .monto(request.getMonto())
                .esSena(Boolean.TRUE.equals(request.getEsSena()))
                .esTrueque(esTrueque)
                .detalleTrueque(esTrueque ? request.getDetalleTrueque().trim() : null)
                .fecha(request.getFecha() != null ? request.getFecha() : LocalDateTime.now())
                .build();

        Pago guardado = pagoRepository.save(pago);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorTurno(UUID turnoId) {
        buscarTurnoPropio(turnoId);
        return pagoRepository.findByTurnoIdOrderByFechaAsc(turnoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResumenPagoResponse obtenerResumen(UUID turnoId) {
        Turno turno = buscarTurnoPropio(turnoId);

        List<PagoResponse> pagos = pagoRepository.findByTurnoIdOrderByFechaAsc(turnoId)
                .stream()
                .map(this::toResponse)
                .toList();

        BigDecimal montoPagado = obtenerMontoPagado(turnoId);
        BigDecimal deuda = turno.getMontoTotal().subtract(montoPagado);
        if (deuda.compareTo(BigDecimal.ZERO) < 0) {
            deuda = BigDecimal.ZERO;
        }

        return ResumenPagoResponse.builder()
                .turnoId(turno.getId())
                .montoTotal(turno.getMontoTotal())
                .montoPagado(montoPagado)
                .deuda(deuda)
                .tieneDeuda(deuda.compareTo(BigDecimal.ZERO) > 0)
                .pagos(pagos)
                .build();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorProfesional() {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();

        return pagoRepository.findByTurno_Profesional_Id(profesionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResumenDiarioPagoResponse obtenerResumenDiario(LocalDate fecha) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        LocalDate dia = fecha != null ? fecha : LocalDate.now();
        LocalDateTime desde = dia.atStartOfDay();
        LocalDateTime hasta = dia.plusDays(1).atStartOfDay();

        List<PagoResponse> pagos = pagoRepository
                .findByTurno_Profesional_IdAndFechaGreaterThanEqualAndFechaLessThanOrderByFechaAsc(profesionalId, desde, hasta)
                .stream()
                .map(this::toResponse)
                .toList();

        BigDecimal totalRecaudado = pagoRepository.sumMontoByProfesionalIdAndFechaRango(profesionalId, desde, hasta);

        return ResumenDiarioPagoResponse.builder()
                .fecha(dia)
                .totalRecaudado(totalRecaudado != null ? totalRecaudado : BigDecimal.ZERO)
                .cantidadPagos(pagos.size())
                .pagos(pagos)
                .build();
    }

    @Transactional
    public void eliminar(UUID id) {
        Pago pago = buscarPagoPropio(id);

        if (EstadoTurno.REALIZADO.equals(pago.getTurno().getEstado())) {
            throw new IllegalArgumentException("No se puede eliminar un pago de un turno en estado REALIZADO");
        }

        pagoRepository.delete(pago);
    }

    private Turno buscarTurnoPropio(UUID turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el turno con ID: " + turnoId));
        validarTurnoPerteneceAProfesionalAutenticada(turno);
        return turno;
    }

    private Pago buscarPagoPropio(UUID id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el pago con ID: " + id));
        validarTurnoPerteneceAProfesionalAutenticada(pago.getTurno());
        return pago;
    }

    private void validarTurnoPerteneceAProfesionalAutenticada(Turno turno) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        if (!turno.getProfesional().getId().equals(profesionalId)) {
            throw new AccesoNoAutorizadoException("No se encontró el turno con ID: " + turno.getId());
        }
    }

    private void validarMetodo(MetodoPago metodo) {
        if (metodo == null) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
    }

    private void validarMontoMayorACero(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
    }

    private void validarTrueque(PagoRequest request) {
        if (MetodoPago.TRUEQUE.equals(request.getMetodo())
                && (request.getDetalleTrueque() == null || request.getDetalleTrueque().isBlank())) {
            throw new IllegalArgumentException("El detalle del trueque es obligatorio cuando el método de pago es TRUEQUE");
        }
    }

    private void validarNoSuperaMontoTotal(Turno turno, BigDecimal nuevoMonto) {
        BigDecimal montoPagado = obtenerMontoPagado(turno.getId());
        BigDecimal totalConNuevoPago = montoPagado.add(nuevoMonto);

        if (totalConNuevoPago.compareTo(turno.getMontoTotal()) > 0) {
            throw new IllegalArgumentException(
                    "El pago supera el monto total del turno. Monto total: " + turno.getMontoTotal()
                            + ", ya pagado: " + montoPagado
                            + ", nuevo pago: " + nuevoMonto);
        }
    }

    private BigDecimal obtenerMontoPagado(UUID turnoId) {
        BigDecimal montoPagado = pagoRepository.sumMontoByTurnoId(turnoId);
        return montoPagado != null ? montoPagado : BigDecimal.ZERO;
    }

    private PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .turnoId(pago.getTurno().getId())
                .metodo(pago.getMetodo())
                .monto(pago.getMonto())
                .esSena(pago.getEsSena())
                .esTrueque(pago.getEsTrueque())
                .detalleTrueque(pago.getDetalleTrueque())
                .fecha(pago.getFecha())
                .creadoEn(pago.getCreadoEn())
                .actualizadoEn(pago.getActualizadoEn())
                .build();
    }
}


