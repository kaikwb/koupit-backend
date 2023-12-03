package br.com.fiap.koupitbackend.controllers;

import br.com.fiap.koupitbackend.models.Product;
import br.com.fiap.koupitbackend.models.PurchaseRequest;
import br.com.fiap.koupitbackend.payload.request.ProductPurchaseRequestPayload;
import br.com.fiap.koupitbackend.payload.request.PurchaseRequestCreate;
import br.com.fiap.koupitbackend.payload.response.PurchaseRequestResponse;
import br.com.fiap.koupitbackend.repositories.ProductRepository;
import br.com.fiap.koupitbackend.repositories.PurchaseRequestRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchase-requests")
public class PurchaseRequestController {
    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<PurchaseRequestResponse>> getPurchaseRequests(@PageableDefault(sort = {"id"}) final Pageable pageable) {
        List<PurchaseRequest> purchaseRequests = purchaseRequestRepository.findAll(pageable).toList();

        List<PurchaseRequestResponse> purchaseRequestResponses = purchaseRequests.stream().map(PurchaseRequestResponse::fromPurchaseRequest).toList();

        return ResponseEntity.ok(purchaseRequestResponses);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<PurchaseRequestResponse> createPurchaseRequest(@RequestBody @Valid final PurchaseRequestCreate purchaseRequestCreate) {
        Map<Long, Integer> productAndQuantity = purchaseRequestCreate.getProducts().stream().collect(Collectors.toMap(ProductPurchaseRequestPayload::getId, ProductPurchaseRequestPayload::getQuantity));

        List<Product> productsList = productRepository.findAllById(productAndQuantity.keySet());

        if (productsList.size() != productAndQuantity.size()) {
            return ResponseEntity.badRequest().build();
        }

        Map<Product, Integer> products = productsList.stream().map(product -> Map.entry(product, productAndQuantity.get(product.getId()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        PurchaseRequest purchaseRequest = PurchaseRequest.
            createFromProductMap(products);

        purchaseRequestRepository.save(purchaseRequest);

        return ResponseEntity.created(URI.create("")).body(PurchaseRequestResponse.fromPurchaseRequest(purchaseRequest));
    }

    @Transactional
    @PutMapping("/{id}/approve")
    public ResponseEntity<PurchaseRequestResponse> approvePurchaseRequest(@PathVariable final Long id) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(id).orElse(null);

        if (purchaseRequest == null) {
            return ResponseEntity.notFound().build();
        }

        purchaseRequest.setPending(false);

        purchaseRequestRepository.save(purchaseRequest);

        return ResponseEntity.ok(PurchaseRequestResponse.fromPurchaseRequest(purchaseRequest));
    }
}
