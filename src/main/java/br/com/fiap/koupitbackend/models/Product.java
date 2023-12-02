package br.com.fiap.koupitbackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "products"
)
public class Product {
    @Id
    @SequenceGenerator(name = "sq_products", sequenceName = "sq_products", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_products")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "brand_id",
        foreignKey = @ForeignKey(name = "fk_products_brand")
    )
    private ProductBrand brand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "products_product_types",
        joinColumns = @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_product_types_products")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "product_type_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_product_types_product_types")
        ),
        uniqueConstraints = @UniqueConstraint(name = "uk_products_product_types", columnNames = {"product_id", "product_type_id"})
    )
    private Set<ProductType> types;
}
