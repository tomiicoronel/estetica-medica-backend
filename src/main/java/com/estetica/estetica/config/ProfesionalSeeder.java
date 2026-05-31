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
        completarCredencialesFaltantes();

        if (!profesionalRepository.existsByRol(RolUsuario.ADMIN)) {
            profesionalRepository.save(crearAdmin());
        }
    }

    private void completarCredencialesFaltantes() {
        List<Profesional> profesionales = profesionalRepository.findAll();
        boolean huboCambios = false;

        for (Profesional profesional : profesionales) {
            if (profesional.getPassword() == null || profesional.getPassword().isBlank()) {
                profesional.setPassword(passwordEncoder.encode(PASSWORD_INICIAL));
                profesional.setDebeCambiarPassword(true);
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

    private Profesional crearAdmin() {
        return Profesional.builder()
                .nombre("Admin")
                .apellido("Sistema")
                .email("admin@estetica.local")
                .telefono("0000000000")
                .especialidad("Administración")
                .password(passwordEncoder.encode(PASSWORD_INICIAL))
                .debeCambiarPassword(true)
                .rol(RolUsuario.ADMIN)
                .build();
    }
}


