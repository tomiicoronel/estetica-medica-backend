package com.estetica.estetica.repository;

import com.estetica.estetica.model.HistoriaClinicaFacial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoriaClinicaFacialRepository extends JpaRepository<HistoriaClinicaFacial, UUID> {

    Optional<HistoriaClinicaFacial> findByPacienteId(UUID pacienteId);

    boolean existsByPacienteId(UUID pacienteId);
}
