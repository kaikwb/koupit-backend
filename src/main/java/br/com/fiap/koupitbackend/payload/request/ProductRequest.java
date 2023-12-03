package br.com.fiap.koupitbackend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank
    private final String name;

    private final String    description;

    private final String brand;

    @NotEmpty
    private final String[] types;
}
