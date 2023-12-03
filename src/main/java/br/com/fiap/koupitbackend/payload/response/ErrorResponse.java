package br.com.fiap.koupitbackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String error;

    public static ErrorResponse fromException(final Exception exception) {
        return ErrorResponse.builder()
            .error(exception.getMessage())
            .build();
    }
}
