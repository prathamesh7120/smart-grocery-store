package com.grocery.grocery_backend.controller;

import com.grocery.grocery_backend.dto.ApiResponse;
import com.grocery.grocery_backend.dto.ProductRequest;
import com.grocery.grocery_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ─── Public endpoints ─────────────────────────────────

    @GetMapping("/api/products")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        var products = productService.getAllProducts(
                category, search, minPrice, maxPrice);
        return ResponseEntity.ok(
                new ApiResponse(true, "Products fetched", products));
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable String id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product fetched", product));
    }

    // ─── Admin endpoints ──────────────────────────────────

    @PostMapping("/api/admin/products")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody ProductRequest request) {
        var product = productService.createProduct(request);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product created", product));
    }

    @PutMapping("/api/admin/products/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {
        var product = productService.updateProduct(id, request);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product updated", product));
    }

    @DeleteMapping("/api/admin/products/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product deleted"));
    }
}
