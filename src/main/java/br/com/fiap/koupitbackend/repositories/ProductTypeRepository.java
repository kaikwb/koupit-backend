package br.com.fiap.koupitbackend.repositories;

import br.com.fiap.koupitbackend.models.ProductType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    Optional<ProductType> findByName(final String name);

    default ProductType findByNameOrThrow(final String name) throws EntityNotFoundException {
        Optional<ProductType> productType = this.findByName(name);

        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Product type [%s] not found".formatted(name));
        }

        return productType.get();
    }
}
