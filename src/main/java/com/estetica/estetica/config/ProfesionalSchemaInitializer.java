package com.estetica.estetica.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProfesionalSchemaInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("ALTER TABLE profesionales DROP CONSTRAINT IF EXISTS profesionales_rol_check");
        jdbcTemplate.execute("""
                ALTER TABLE profesionales
                ADD CONSTRAINT profesionales_rol_check
                CHECK (rol IN ('ADMIN', 'PROFESIONAL', 'PACIENTE'))
                """);
    }
}
