package br.com.fiap.koupitbackend.repositories;

import br.com.fiap.koupitbackend.models.ProductBrand;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {
    ProductBrand findByName(final String name) throws EntityNotFoundException;
}
