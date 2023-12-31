package br.com.fiap.koupitbackend.payload.response;

import br.com.fiap.koupitbackend.models.Product;
import br.com.fiap.koupitbackend.models.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductResponse {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String brand;

    @NotNull
    private Set<String> types;

    public static ProductResponse fromProduct(final Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .brand(product.getBrand() != null ? product.getBrand().getName() : null)
            .types(product.getTypes() != null && !product.getTypes().isEmpty() ? product.getTypes().stream().map(ProductType::getName).collect(Collectors.toSet()) : null)
            .build();
    }
}
