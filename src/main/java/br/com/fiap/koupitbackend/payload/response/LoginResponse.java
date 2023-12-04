package br.com.fiap.koupitbackend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    @NotBlank
    @JsonProperty("access_token")
    private String accessToken;

    @NotNull
    @JsonProperty("expires_in")
    private Long expiresIn;

    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;

    @NotBlank
    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    @NotBlank
    @JsonProperty("token_type")
    private String tokenType;

    @NotNull
    @JsonProperty("not-before-policy")
    private Long notBeforePolicy;

    @NotBlank
    @JsonProperty("session_state")
    private String sessionState;

    @NotBlank
    @JsonProperty("scope")
    private String scope;

    public static LoginResponse fromJsonNode(final JsonNode jsonNode) {
        return LoginResponse.builder()
            .accessToken(jsonNode.get("access_token").asText())
            .expiresIn(jsonNode.get("expires_in").asLong())
            .refreshToken(jsonNode.get("refresh_token").asText())
            .refreshExpiresIn(jsonNode.get("refresh_expires_in").asLong())
            .tokenType(jsonNode.get("token_type").asText())
            .notBeforePolicy(jsonNode.get("not-before-policy").asLong())
            .sessionState(jsonNode.get("session_state").asText())
            .scope(jsonNode.get("scope").asText())
            .build();
    }
}
