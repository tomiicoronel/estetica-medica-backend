package com.estetica.estetica.repository;

import com.estetica.estetica.model.TurnoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TurnoServicioRepository extends JpaRepository<TurnoServicio, UUID> {

    List<TurnoServicio> findByTurnoId(UUID turnoId);
}
