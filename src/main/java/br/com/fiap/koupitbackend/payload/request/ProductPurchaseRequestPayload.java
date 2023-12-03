package br.com.fiap.koupitbackend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPurchaseRequestPayload {
    @NotNull
    private Long id;

    @NotNull
    private Integer quantity;
}
