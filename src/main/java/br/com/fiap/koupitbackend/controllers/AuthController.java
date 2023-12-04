package br.com.fiap.koupitbackend.controllers;

import br.com.fiap.koupitbackend.payload.request.LoginRequest;
import br.com.fiap.koupitbackend.payload.response.LoginResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${app.keycloak.auth-server-url}")
    String authServerUrl;

    @Value("${app.keycloak.client-id}")
    String clientId;

    @Value("${app.keycloak.client-secret}")
    String clientSecret;

    URL makeUrl(String path) throws MalformedURLException {
        return URI.create(authServerUrl + path).toURL();
    }

    HttpURLConnection makeConnection(String path) throws IOException {
        return (HttpURLConnection) makeUrl(path).openConnection();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            HttpURLConnection connection = makeConnection("");

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String postData = String.format("client_id=%s&client_secret=%s&grant_type=password&username=%s&password=%s",
                clientId, clientSecret, username, password);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            ObjectMapper objectMapper = new ObjectMapper();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseBody = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                JsonNode jsonResponse = objectMapper.readTree(responseBody);

                LoginResponse loginResponse = LoginResponse.fromJsonNode(jsonResponse);

                return ResponseEntity.ok(loginResponse);
            } else {
                String errorBody = new String(connection.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
                JsonNode jsonError = objectMapper.readTree(errorBody);

                logger.error("Error while trying to login: %s - %s".formatted(responseCode, jsonError));

                throw new ResponseStatusException(HttpStatus.valueOf(responseCode), jsonError.toString());
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while trying to login");
        }
    }
}
