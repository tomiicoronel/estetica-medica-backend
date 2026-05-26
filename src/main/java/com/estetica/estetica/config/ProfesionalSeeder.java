package com.estetica.estetica.config;

import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.RolUsuario;
import com.estetica.estetica.repository.ProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfesionalSeeder implements CommandLineRunner {

    private static final String PASSWORD_INICIAL = "Password123!";

    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (profesionalRepository.count() > 0) {
            completarCredencialesFaltantes();
            return;
        }

        String passwordHash = passwordEncoder.encode(PASSWORD_INICIAL);

        List<Profesional> profesionalesIniciales = List.of(
                crearProfesional("Ana", "López", "ana.lopez@estetica.local", "1123456789", "Cosmetología", passwordHash),
                crearProfesional("María", "González", "maria.gonzalez@estetica.local", "1123456790", "Dermatología estética", passwordHash),
                crearProfesional("Laura", "Martínez", "laura.martinez@estetica.local", "1123456791", "Estética corporal", passwordHash),
                crearProfesional("Sofía", "Pérez", "sofia.perez@estetica.local", "1123456792", "Tratamientos faciales", passwordHash),
                crearProfesional("Camila", "Rodríguez", "camila.rodriguez@estetica.local", "1123456793", "Depilación y aparatología", passwordHash)
        );

        profesionalRepository.saveAll(profesionalesIniciales);
    }

    private void completarCredencialesFaltantes() {
        List<Profesional> profesionales = profesionalRepository.findAll();
        boolean huboCambios = false;

        for (Profesional profesional : profesionales) {
            if (profesional.getPassword() == null || profesional.getPassword().isBlank()) {
                profesional.setPassword(passwordEncoder.encode(PASSWORD_INICIAL));
                huboCambios = true;
            }
            if (profesional.getRol() == null) {
                profesional.setRol(RolUsuario.PROFESIONAL);
                huboCambios = true;
            }
        }

        if (huboCambios) {
            profesionalRepository.saveAll(profesionales);
        }
    }

    private Profesional crearProfesional(String nombre,
                                         String apellido,
                                         String email,
                                         String telefono,
                                         String especialidad,
                                         String passwordHash) {
        return Profesional.builder()
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .telefono(telefono)
                .especialidad(especialidad)
                .password(passwordHash)
                .rol(RolUsuario.PROFESIONAL)
                .build();
    }
}


