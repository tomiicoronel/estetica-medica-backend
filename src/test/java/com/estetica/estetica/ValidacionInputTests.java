package com.estetica.estetica;

import com.estetica.estetica.model.Profesional;
import com.estetica.estetica.model.RolUsuario;
import com.estetica.estetica.repository.PacienteRepository;
import com.estetica.estetica.repository.ProfesionalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidacionInputTests {

    private static final String EMAIL = "validacion.test@estetica.local";
    private static final String PASSWORD = "Password123!";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void prepararProfesional() {
        prepararProfesional(false);
    }

    @AfterEach
    void limpiarDatos() {
        profesionalRepository.findByEmail(EMAIL).ifPresent(profesional -> {
            pacienteRepository.findByProfesionalId(profesional.getId())
                    .forEach(pacienteRepository::delete);
            pacienteRepository.flush();
            profesionalRepository.delete(profesional);
            profesionalRepository.flush();
        });
    }

    @Test
    void datosInvalidosDevuelven400YNo500() throws Exception {
        String token = login();

        // 1. DNI con letras: debe rechazarse con 400, no guardarse.
        HttpResponse<String> dniConLetras = postJson("/api/pacientes", token, """
                {
                  "nombre": "Paciente",
                  "apellido": "DniInvalido",
                  "dniCuit": "asdasda",
                  "telefono": "3510000000"
                }
                """);
        assertThat(dniConLetras.statusCode()).isEqualTo(400);
        JsonNode dniBody = objectMapper.readTree(dniConLetras.body());
        assertThat(dniBody.get("mensajes").has("dniCuit")).isTrue();

        // 2. Teléfono con letras: también 400.
        HttpResponse<String> telefonoConLetras = postJson("/api/pacientes", token, """
                {
                  "nombre": "Paciente",
                  "apellido": "TelefonoInvalido",
                  "dniCuit": "30111222",
                  "telefono": "no-es-telefono"
                }
                """);
        assertThat(telefonoConLetras.statusCode()).isEqualTo(400);
        assertThat(objectMapper.readTree(telefonoConLetras.body()).get("mensajes").has("telefono")).isTrue();

        // 3. Paciente válido: happy path sigue funcionando (201).
        String dniValido = String.valueOf(System.currentTimeMillis() % 100000000L);
        HttpResponse<String> pacienteValido = postJson("/api/pacientes", token, """
                {
                  "nombre": "Paciente",
                  "apellido": "Valido",
                  "dniCuit": "%s",
                  "telefono": "+54 351 000-0000"
                }
                """.formatted(dniValido));
        assertThat(pacienteValido.statusCode()).isEqualTo(201);

        // 4. Historia clínica facial con un campo de texto excediendo el límite (255):
        //    la validación corre antes que el service, por lo que devuelve 400 y no 500
        //    aunque el paciente del path no exista.
        String fumLargo = "a".repeat(300);
        HttpResponse<String> historiaLarga = postJson(
                "/api/pacientes/" + UUID.randomUUID() + "/historia-clinica-facial", token, """
                {
                  "fum": "%s"
                }
                """.formatted(fumLargo));
        assertThat(historiaLarga.statusCode()).isEqualTo(400);
        assertThat(objectMapper.readTree(historiaLarga.body()).get("mensajes").has("fum")).isTrue();
    }

    private String login() throws Exception {
        HttpResponse<String> respuesta = postJson("/api/auth/login", null, """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(EMAIL, PASSWORD));
        assertThat(respuesta.statusCode()).isEqualTo(200);
        return objectMapper.readTree(respuesta.body()).get("token").asText();
    }

    private void prepararProfesional(boolean debeCambiarPassword) {
        Profesional profesional = profesionalRepository.findByEmail(EMAIL)
                .orElseGet(() -> Profesional.builder()
                        .nombre("Validacion")
                        .apellido("Test")
                        .email(EMAIL)
                        .telefono("3510000030")
                        .especialidad("Test")
                        .build());
        profesional.setPassword(passwordEncoder.encode(PASSWORD));
        profesional.setDebeCambiarPassword(debeCambiarPassword);
        profesional.setRol(RolUsuario.PROFESIONAL);
        profesionalRepository.saveAndFlush(profesional);
    }

    private HttpResponse<String> postJson(String path, String token, String body) throws Exception {
        HttpRequest.Builder builder = request(path)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
    }
}
