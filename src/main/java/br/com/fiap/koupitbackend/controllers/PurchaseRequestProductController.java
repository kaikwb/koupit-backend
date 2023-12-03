package br.com.fiap.koupitbackend.controllers;

import br.com.fiap.koupitbackend.models.Product;
import br.com.fiap.koupitbackend.models.PurchaseRequest;
import br.com.fiap.koupitbackend.payload.request.ProductPurchaseRequestPayload;
import br.com.fiap.koupitbackend.payload.request.ProductQuantityUpdateRequest;
import br.com.fiap.koupitbackend.payload.response.ErrorResponse;
import br.com.fiap.koupitbackend.payload.response.PurchaseRequestResponse;
import br.com.fiap.koupitbackend.repositories.ProductRepository;
import br.com.fiap.koupitbackend.repositories.PurchaseRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/purchase-request/{purchaseRequestId}/products")
public class PurchaseRequestProductController {
    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private ProductRepository productRepository;

    PurchaseRequest getPurchaseRequest(final Long purchaseRequestId) {
        Optional<PurchaseRequest> purchaseRequestR = purchaseRequestRepository.findById(purchaseRequestId);

        if (purchaseRequestR.isEmpty()) {
            throw new EntityNotFoundException("Purchase request id [%s] not found".formatted(purchaseRequestId));
        }

        return purchaseRequestR.get();
    }

    Product getProduct(final Long productId) {
        Optional<Product> productR = productRepository.findById(productId);

        if (productR.isEmpty()) {
            throw new EntityNotFoundException("Product id [%s] not found".formatted(productId));
        }

        return productR.get();
    }


    @Transactional
    @PostMapping
    public ResponseEntity<?> addProductToPurchaseRequest(@PathVariable final Long purchaseRequestId, @RequestBody @Valid final ProductPurchaseRequestPayload productPayload) {
        PurchaseRequest purchaseRequest = getPurchaseRequest(purchaseRequestId);
        Product product = getProduct(productPayload.getId());

        if (purchaseRequest.getProducts().containsKey(product)) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Product id [%s] already added to purchase request id [%s]".formatted(productPayload.getId(), purchaseRequestId))
                .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        purchaseRequest.getProducts().put(product, productPayload.getQuantity());

        purchaseRequestRepository.save(purchaseRequest);

        PurchaseRequestResponse response = PurchaseRequestResponse.fromPurchaseRequest(purchaseRequest);

        return ResponseEntity.ok(response);
    }

    @Transactional
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProductQuantityInPurchaseRequest(@PathVariable final Long purchaseRequestId, @PathVariable final Long productId, @RequestBody @Valid final ProductQuantityUpdateRequest quantityUpdateRequest) {
        PurchaseRequest purchaseRequest = getPurchaseRequest(purchaseRequestId);
        Product product = getProduct(productId);

        if (!purchaseRequest.getProducts().containsKey(product)) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Product id [%s] not added to purchase request id [%s]".formatted(productId, purchaseRequestId))
                .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        purchaseRequest.getProducts().put(product, quantityUpdateRequest.getQuantity());

        purchaseRequestRepository.save(purchaseRequest);

        PurchaseRequestResponse response = PurchaseRequestResponse.fromPurchaseRequest(purchaseRequest);

        return ResponseEntity.ok(response);
    }

    @Transactional
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeProductFromPurchaseRequest(@PathVariable final Long purchaseRequestId, @PathVariable final Long productId) {
        PurchaseRequest purchaseRequest = getPurchaseRequest(purchaseRequestId);
        Product product = getProduct(productId);

        if (!purchaseRequest.getProducts().containsKey(product)) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Product id [%s] not added to purchase request id [%s]".formatted(productId, purchaseRequestId))
                .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        purchaseRequest.getProducts().remove(product);

        purchaseRequestRepository.save(purchaseRequest);

        PurchaseRequestResponse response = PurchaseRequestResponse.fromPurchaseRequest(purchaseRequest);

        return ResponseEntity.ok(response);
    }
}
