package com.estetica.estetica.repository;

import com.estetica.estetica.model.EstadoTurno;
import com.estetica.estetica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, UUID> {

    List<Turno> findByProfesionalId(UUID profesionalId);

    List<Turno> findByProfesionalIdAndEstado(UUID profesionalId, EstadoTurno estado);

    List<Turno> findByPacienteId(UUID pacienteId);

    List<Turno> findByProfesionalIdAndFechaHoraBetween(UUID profesionalId, LocalDateTime desde, LocalDateTime hasta);
}
