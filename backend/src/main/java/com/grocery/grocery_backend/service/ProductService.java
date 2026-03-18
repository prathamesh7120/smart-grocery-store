package com.grocery.grocery_backend.service;

import com.grocery.grocery_backend.dto.ProductRequest;
import com.grocery.grocery_backend.model.Product;
import com.grocery.grocery_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ─── Public ───────────────────────────────────────────

    public List<Product> getAllProducts(String category,
                                        String search,
                                        Double minPrice,
                                        Double maxPrice) {

        boolean hasSearch   = search    != null && !search.isBlank();
        boolean hasCategory = category  != null && !category.isBlank();
        boolean hasPrice    = minPrice  != null && maxPrice != null;

        if (hasSearch && hasPrice) {
            return productRepository.searchWithFilters(
                    search,
                    minPrice,
                    maxPrice);
        }
        if (hasSearch) {
            return productRepository.searchByName(search);
        }
        if (hasCategory) {
            return productRepository.findByCategoryAndIsActiveTrue(category);
        }
        if (hasPrice) {
            return productRepository.findByPriceRange(minPrice, maxPrice);
        }

        return productRepository.findByIsActiveTrue();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ─── Admin ────────────────────────────────────────────

    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        mapRequestToProduct(request, product);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        mapRequestToProduct(request, product);
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);          // soft delete
        productRepository.save(product);
    }

    // ─── Helper ───────────────────────────────────────────

    private void mapRequestToProduct(ProductRequest req, Product p) {
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setCategory(req.getCategory());
        p.setBrand(req.getBrand());
        p.setStock(req.getStock());
        p.setImageUrl(req.getImageUrl());
        p.setDiscountPercent(req.getDiscountPercent());
    }
}
