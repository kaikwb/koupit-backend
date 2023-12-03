package br.com.fiap.koupitbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "purchase_requests"
)
public class PurchaseRequest {
    @Id
    @SequenceGenerator(name = "sq_purchase_requests", sequenceName = "sq_purchase_requests", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_purchase_requests")
    private Long id;

    @Column(name = "request_date", nullable = false)
    private ZonedDateTime requestDate = ZonedDateTime.now(ZoneOffset.UTC);

    @Column(name = "pending", nullable = false)
    private Boolean pending = true;

    @ElementCollection
    @CollectionTable(
        name = "purchase_requests_products",
        joinColumns = @JoinColumn(
            name = "purchase_request_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_requests_products_purchase_request")
        ),
        uniqueConstraints = @UniqueConstraint(name = "uk_purchase_requests_products", columnNames = {"purchase_request_id", "product_id"})
    )
    @MapKeyJoinColumn(
        name = "product_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_purchase_requests_products_products")
    )
    @Column(name = "quantity", nullable = false)
    private Map<Product, Integer> products;

    public static PurchaseRequest createFromProductMap(Map<Product, Integer> products) {
        return PurchaseRequest.builder()
            .requestDate(ZonedDateTime.now(ZoneOffset.UTC))
            .pending(true)
            .products(products)
            .build();
    }
}
