package br.com.fiap.koupitbackend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String error;

    public static ErrorResponse fromException(final Exception exception) {
        return ErrorResponse.builder()
            .error(exception.getMessage())
            .build();
    }
}
