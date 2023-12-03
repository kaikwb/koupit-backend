package br.com.fiap.koupitbackend.payload.response;

import br.com.fiap.koupitbackend.models.PurchaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequestResponse {
    private Long id;
    private ZonedDateTime requestDate;
    private Boolean pending;
    private ProductPurchaseRequestResponse[] products;

    public static PurchaseRequestResponse fromPurchaseRequest(final PurchaseRequest purchaseRequest) {
        return PurchaseRequestResponse.builder()
            .id(purchaseRequest.getId())
            .requestDate(purchaseRequest.getRequestDate())
            .pending(purchaseRequest.getPending())
            .products(purchaseRequest.getProducts().entrySet().stream().map(entry -> ProductPurchaseRequestResponse.fromProduct(entry.getKey(), entry.getValue())).toArray(ProductPurchaseRequestResponse[]::new))
            .build();
    }
}
