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
    name = "product_types",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_types_name", columnNames = "name")
    }
)
public class ProductType {
    @Id
    @SequenceGenerator(name = "sq_product_types", sequenceName = "sq_product_types", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_product_types")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
