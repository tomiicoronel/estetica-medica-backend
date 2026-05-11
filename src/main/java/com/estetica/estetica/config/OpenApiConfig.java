package com.estetica.estetica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI esteticaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Estética")
                        .version("1.0.0")
                        .description("API REST para gestión de profesionales, pacientes, servicios, historias clínicas y turnos.")
                        .contact(new Contact()
                                .name("Estética")
                                .email("soporte@estetica.local")));
    }
}

