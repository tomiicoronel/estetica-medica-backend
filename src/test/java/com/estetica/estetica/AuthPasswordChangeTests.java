package com.estetica.estetica;

import com.estetica.estetica.model.Profesional;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthPasswordChangeTests {

    private static final String FRONTEND_ORIGIN = "http://localhost:5173";
    private static final String EMAIL_SEEDER = "ana.lopez@estetica.local";
    private static final String PASSWORD_INICIAL = "Password123!";
    private static final String PASSWORD_NUEVA = "PasswordNueva123!";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void prepararProfesionalSeeder() {
        restaurarPasswordInicial();
    }

    @AfterEach
    void limpiarProfesionalSeeder() {
        restaurarPasswordInicial();
    }

    @Test
    void flujoCompletoCambioPasswordActualizaFlagYCorsPermiteFrontend() throws Exception {
        HttpResponse<String> loginInicial = postJson("/api/auth/login", null, """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(EMAIL_SEEDER, PASSWORD_INICIAL));

        JsonNode loginInicialBody = objectMapper.readTree(loginInicial.body());
        String token = loginInicialBody.get("token").asText();

        assertThat(loginInicial.statusCode()).isEqualTo(200);
        assertThat(loginInicialBody.get("debeCambiarPassword").asBoolean()).isTrue();

        HttpResponse<String> preflight = optionsCors("/api/auth/cambiar-password");
        assertThat(preflight.statusCode()).isEqualTo(200);
        assertThat(preflight.headers().firstValue("Access-Control-Allow-Origin")).contains(FRONTEND_ORIGIN);
        assertThat(preflight.headers().firstValue("Access-Control-Allow-Methods").orElse(""))
                .contains("POST")
                .contains("OPTIONS");
        assertThat(preflight.headers().firstValue("Access-Control-Allow-Headers").orElse("").toLowerCase())
                .contains("authorization")
                .contains("content-type");

        HttpResponse<String> cambioIncorrecto = postJson("/api/auth/cambiar-password", token, """
                {
                  "passwordActual": "PasswordIncorrecta123!",
                  "passwordNueva": "%s"
                }
                """.formatted(PASSWORD_NUEVA));
        assertThat(cambioIncorrecto.statusCode()).isEqualTo(401);

        HttpResponse<String> cambioCorrecto = postJson("/api/auth/cambiar-password", token, """
                {
                  "passwordActual": "%s",
                  "passwordNueva": "%s"
                }
                """.formatted(PASSWORD_INICIAL, PASSWORD_NUEVA));
        assertThat(cambioCorrecto.statusCode()).isEqualTo(204);

        HttpResponse<String> loginFinal = postJson("/api/auth/login", null, """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(EMAIL_SEEDER, PASSWORD_NUEVA));

        JsonNode loginFinalBody = objectMapper.readTree(loginFinal.body());
        assertThat(loginFinal.statusCode()).isEqualTo(200);
        assertThat(loginFinalBody.get("debeCambiarPassword").asBoolean()).isFalse();
    }

    @Test
    void cambiarPasswordRequiereToken() throws Exception {
        HttpResponse<String> response = postJson("/api/auth/cambiar-password", null, """
                {
                  "passwordActual": "%s",
                  "passwordNueva": "%s"
                }
                """.formatted(PASSWORD_INICIAL, PASSWORD_NUEVA));

        assertThat(response.statusCode()).isEqualTo(401);
    }

    private void restaurarPasswordInicial() {
        Profesional profesional = profesionalRepository.findByEmail(EMAIL_SEEDER)
                .orElseThrow(() -> new IllegalStateException("No existe la profesional del seeder: " + EMAIL_SEEDER));
        profesional.setPassword(passwordEncoder.encode(PASSWORD_INICIAL));
        profesional.setDebeCambiarPassword(true);
        profesionalRepository.saveAndFlush(profesional);
    }

    private HttpResponse<String> postJson(String path, String token, String body) throws Exception {
        HttpRequest.Builder builder = request(path)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        agregarAuthorization(builder, token);
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> optionsCors(String path) throws Exception {
        HttpRequest request = request(path)
                .header("Origin", FRONTEND_ORIGIN)
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Authorization, Content-Type")
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
    }

    private void agregarAuthorization(HttpRequest.Builder builder, String token) {
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
    }
}
