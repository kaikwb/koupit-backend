package br.com.fiap.koupitbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "product_brands",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_brands_name", columnNames = "name")
    }
)
public class ProductBrand {
    @Id
    @SequenceGenerator(name = "sq_product_brands", sequenceName = "sq_product_brands", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_product_brands")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
