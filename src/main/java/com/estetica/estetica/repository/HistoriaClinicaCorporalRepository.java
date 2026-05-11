package com.estetica.estetica.repository;

import com.estetica.estetica.model.HistoriaClinicaCorporal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoriaClinicaCorporalRepository extends JpaRepository<HistoriaClinicaCorporal, UUID> {

    Optional<HistoriaClinicaCorporal> findByPacienteId(UUID pacienteId);

    boolean existsByPacienteId(UUID pacienteId);
}
