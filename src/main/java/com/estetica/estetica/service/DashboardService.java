package com.estetica.estetica.service;

import com.estetica.estetica.dto.response.DashboardResponse;
import com.estetica.estetica.model.EstadoTurno;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.PagoRepository;
import com.estetica.estetica.repository.TurnoRepository;
import com.estetica.estetica.security.ProfesionalAutenticadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final PagoRepository pagoRepository;
    private final ProfesionalAutenticadaService profesionalAutenticadaService;

    @Transactional(readOnly = true)
    public DashboardResponse obtenerResumen(LocalDate fecha) {
        UUID profesionalId = profesionalAutenticadaService.obtenerIdProfesionalAutenticada();
        LocalDate dia = fecha != null ? fecha : LocalDate.now();
        LocalDateTime desde = dia.atStartOfDay();
        LocalDateTime hasta = dia.plusDays(1).atStartOfDay();

        long cantidadTurnos = turnoRepository
                .countByProfesionalIdAndFechaHoraGreaterThanEqualAndFechaHoraLessThan(profesionalId, desde, hasta);

        long cantidadTurnosRealizados = turnoRepository
                .countByProfesionalIdAndEstadoAndFechaHoraGreaterThanEqualAndFechaHoraLessThan(
                        profesionalId, EstadoTurno.REALIZADO, desde, hasta);

        long pacientesActivos = pacienteRepository.countByProfesionalIdAndActivo(profesionalId, true);

        BigDecimal totalRecaudado = pagoRepository.sumMontoByProfesionalIdAndFechaRango(profesionalId, desde, hasta);

        return DashboardResponse.builder()
                .fecha(dia)
                .cantidadTurnos(cantidadTurnos)
                .cantidadTurnosRealizados(cantidadTurnosRealizados)
                .pacientesActivos(pacientesActivos)
                .totalRecaudado(totalRecaudado != null ? totalRecaudado : BigDecimal.ZERO)
                .build();
    }
}
