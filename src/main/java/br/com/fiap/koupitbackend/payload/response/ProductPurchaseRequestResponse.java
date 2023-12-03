package br.com.fiap.koupitbackend.payload.response;

import br.com.fiap.koupitbackend.models.Product;
import br.com.fiap.koupitbackend.models.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ProductPurchaseRequestResponse extends ProductResponse {
    private Integer quantity;

    public static ProductPurchaseRequestResponse fromProduct(final Product product, final Integer quantity) {
        return ProductPurchaseRequestResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .brand(product.getBrand() != null ? product.getBrand().getName() : null)
            .types(product.getTypes() != null && !product.getTypes().isEmpty() ? product.getTypes().stream().map(ProductType::getName).collect(Collectors.toSet()) : null)
            .quantity(quantity)
            .build();
    }
}
