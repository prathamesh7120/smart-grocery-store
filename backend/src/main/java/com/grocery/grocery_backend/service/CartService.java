package com.grocery.grocery_backend.service;

import com.grocery.grocery_backend.dto.AddToCartRequest;
import com.grocery.grocery_backend.dto.UpdateCartRequest;
import com.grocery.grocery_backend.model.Cart;
import com.grocery.grocery_backend.model.Product;
import com.grocery.grocery_backend.repository.CartRepository;
import com.grocery.grocery_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // ─── Get cart ─────────────────────────────────────────

    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    // ─── Add item ─────────────────────────────────────────

    public Cart addToCart(String userId, AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.isActive()) {
            throw new RuntimeException("Product is no longer available");
        }

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Only " + product.getStock() + " items left in stock");
        }

        Cart cart = getCart(userId);

        // Check if product already in cart
        Optional<Cart.CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            // Update quantity
            int newQty = existing.get().getQuantity() + request.getQuantity();
            if (newQty > product.getStock()) {
                throw new RuntimeException("Cannot add more. Only "
                        + product.getStock() + " in stock");
            }
            existing.get().setQuantity(newQty);
        } else {
            // Add new item
            Cart.CartItem item = new Cart.CartItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setImageUrl(product.getImageUrl());
            item.setPrice(product.getPrice());
            item.setDiscountPercent(product.getDiscountPercent());
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    // ─── Update quantity ──────────────────────────────────

    public Cart updateCart(String userId, UpdateCartRequest request) {
        Cart cart = getCart(userId);

        if (request.getQuantity() == 0) {
            // Remove item if quantity set to 0
            cart.getItems().removeIf(
                    i -> i.getProductId().equals(request.getProductId()));
        } else {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (request.getQuantity() > product.getStock()) {
                throw new RuntimeException("Only " + product.getStock() + " in stock");
            }

            cart.getItems().stream()
                    .filter(i -> i.getProductId().equals(request.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not in cart"))
                    .setQuantity(request.getQuantity());
        }

        return cartRepository.save(cart);
    }

    // ─── Remove item ──────────────────────────────────────

    public Cart removeFromCart(String userId, String productId) {
        Cart cart = getCart(userId);

        boolean removed = cart.getItems()
                .removeIf(i -> i.getProductId().equals(productId));

        if (!removed) {
            throw new RuntimeException("Item not found in cart");
        }

        return cartRepository.save(cart);
    }

    // ─── Clear cart ───────────────────────────────────────

    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
