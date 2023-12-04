package br.com.fiap.koupitbackend.controllers;

import br.com.fiap.koupitbackend.models.Product;
import br.com.fiap.koupitbackend.models.ProductBrand;
import br.com.fiap.koupitbackend.payload.request.ProductRequest;
import br.com.fiap.koupitbackend.payload.response.ProductResponse;
import br.com.fiap.koupitbackend.repositories.ProductBrandRepository;
import br.com.fiap.koupitbackend.repositories.ProductRepository;
import br.com.fiap.koupitbackend.repositories.ProductTypeRepository;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    Product fromProductRequest(final ProductRequest productRequest) {
        return Product.builder()
            .name(productRequest.getName())
            .description(productRequest.getDescription())
            .brand(productBrandRepository.findByName(productRequest.getBrand()))
            .types(Arrays.stream(productRequest.getTypes()).map(productTypeRepository::findByNameOrThrow).collect(Collectors.toSet()))
            .build();
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands(@PageableDefault(sort = {"name"}) final Pageable pageable) {
        List<String> brands = productBrandRepository.findAll(pageable).stream().map(ProductBrand::getName).toList();

        return ResponseEntity.ok(brands);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@PageableDefault(sort = {"id"}) final Pageable pageable) {
        List<Product> products = productRepository.findAll(pageable).toList();

        List<ProductResponse> productResponses = products.stream().map(ProductResponse::fromProduct).toList();

        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable final Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (Boolean.FALSE.equals(product.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id [%s] not found".formatted(id));
        }

        return ResponseEntity.ok(ProductResponse.fromProduct(product.get()));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid final ProductRequest productRequest) {
        try {
            Product product = fromProductRequest(productRequest);

            product = productRepository.save(product);

            return ResponseEntity.ok(ProductResponse.fromProduct(product));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable final Long id, @RequestBody @Valid final ProductRequest productRequest) {
        try {
            Optional<Product> productFind = productRepository.findById(id);

            if (Boolean.FALSE.equals(productFind.isPresent())) {
                return ResponseEntity.notFound().build();
            }

            Product productFromRequest = fromProductRequest(productRequest);
            Product product = productFind.get();

            product.setName(productFromRequest.getName());
            product.setDescription(productFromRequest.getDescription());
            product.setBrand(productFromRequest.getBrand());
            product.setTypes(productFromRequest.getTypes());

            productRepository.save(product);

            return ResponseEntity.ok(ProductResponse.fromProduct(product));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (Boolean.FALSE.equals(product.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id [%s] not found".formatted(id));
        }

        productRepository.delete(product.get());

        return ResponseEntity.noContent().build();

    }
}
