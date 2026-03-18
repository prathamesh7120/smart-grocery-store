package com.grocery.grocery_backend.controller;

import com.grocery.grocery_backend.dto.AddToCartRequest;
import com.grocery.grocery_backend.dto.ApiResponse;
import com.grocery.grocery_backend.dto.UpdateCartRequest;
import com.grocery.grocery_backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse> getCart(
            @AuthenticationPrincipal String email) {
        var cart = cartService.getCart(email);
        return ResponseEntity.ok(
                new ApiResponse(true, "Cart fetched", cart));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody AddToCartRequest request) {
        var cart = cartService.addToCart(email, request);
        return ResponseEntity.ok(
                new ApiResponse(true, "Item added to cart", cart));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCart(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateCartRequest request) {
        var cart = cartService.updateCart(email, request);
        return ResponseEntity.ok(
                new ApiResponse(true, "Cart updated", cart));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse> removeItem(
            @AuthenticationPrincipal String email,
            @PathVariable String productId) {
        var cart = cartService.removeFromCart(email, productId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Item removed", cart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(
            @AuthenticationPrincipal String email) {
        cartService.clearCart(email);
        return ResponseEntity.ok(
                new ApiResponse(true, "Cart cleared"));
    }
}
